package ideliance.core.imp;

import ideliance.core.config.ApplicationContext;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.RelationDAO;
import ideliance.core.dao.SubjectDAO;
import ideliance.core.dao.TripletDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Relation;
import ideliance.core.object.Subject;
import ideliance.core.object.Triplet;
import ideliance.core.object.type.TripletType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ImportXML {
	private static final Logger log = Logger.getLogger(ImportXML.class);
	
	private String lang = ApplicationContext.getInstance().getProperty("default.dictionary.lang");
	
	private Document xml = null;
	private XPath xpath = null;
	private DAOFactory daoFactory = null;
	
	private Map<String, Subject> listSubject = null;
	private Map<String, Relation> listRelation = null;
	private List<Triplet> listTriplet = null;

	public ImportXML(InputStream is) {
		DocumentBuilder builder = null;

		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			builder = domFactory.newDocumentBuilder();

			xml = builder.parse(is);
			
			daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		} catch (ParserConfigurationException e) {
			log.error("Error during import initialization", e);
		} catch (SAXException e) {
			log.error("Error during import initialization", e);
		} catch (IOException e) {
			log.error("Error during import initialization", e);
		} catch (DAOException e) {
			log.error("Error during import initialization", e);
		}
		
		xpath = XPathFactory.newInstance().newXPath();
		
		listSubject = new HashMap<String, Subject>();
		listRelation = new HashMap<String, Relation>();
		listTriplet = new ArrayList<Triplet>();
	}
	
	public void load() throws Exception {
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		RelationDAO relationDao = daoFactory.getRelationDAO();
		
		// Traitement des sujets de la section "sujets"
		XPathExpression exprSubject = xpath.compile("/collection/sujets/sujet");
		
		NodeList nodeListSubject = (NodeList) exprSubject.evaluate(xml, XPathConstants.NODESET);
		
		for (int i = 0; i < nodeListSubject.getLength(); i++) {
			Subject s = new Subject();
			
			NodeList nodeListTmpNom = ((Element) nodeListSubject.item(i)).getElementsByTagName("nom");
			NodeList nodeListTmpTexte = ((Element) nodeListSubject.item(i)).getElementsByTagName("texte");
			
			if (nodeListTmpNom.getLength() != 1 || nodeListTmpTexte.getLength() != 1) {
				throw new Exception("XML malformé");
			}
			
			s.setEntitled(lang, nodeListTmpNom.item(0).getTextContent());
			s.setFreeText(lang, nodeListTmpTexte.item(0).getTextContent());
			
			listSubject.put(s.getEntitled(lang), s);
		}
		
		// Traitements des triplets
		exprSubject = xpath.compile("/collection/enonces/enonce");
		
		nodeListSubject = (NodeList) exprSubject.evaluate(xml, XPathConstants.NODESET);
		
		for (int i = 0; i < nodeListSubject.getLength(); i++) {
			Subject s1 = new Subject();
			Subject s2 = new Subject();
			Relation r1 = new Relation();
			Relation r2 = new Relation();
			
			NodeList nodeListTmpSubjets = ((Element) nodeListSubject.item(i)).getElementsByTagName("sujet");
			NodeList nodeListTmpRelation = ((Element) nodeListSubject.item(i)).getElementsByTagName("relation");
			
			if (nodeListTmpSubjets.getLength() != 2 || nodeListTmpRelation.getLength() != 1) {
				throw new Exception("XML malformé");
			}
			
			String entitled1 = nodeListTmpSubjets.item(0).getTextContent();
			String entitled2 = nodeListTmpSubjets.item(1).getTextContent();
			String entitled3 = nodeListTmpRelation.item(0).getTextContent().split(",")[0];
			String entitled4 = nodeListTmpRelation.item(0).getTextContent().split(",")[1];
			
			s1.setEntitled(lang, entitled1);
			s2.setEntitled(lang, entitled2);
			r1.setEntitled(lang, entitled3);
			r2.setEntitled(lang, entitled4);
			
			if (!listSubject.containsKey(entitled1)) {
				listSubject.put(entitled1, s1);
			} else {
				s1 = listSubject.get(entitled1);
			}
			
			if (!listSubject.containsKey(entitled2)) {
				listSubject.put(entitled2, s2);
			} else {
				s2 = listSubject.get(entitled2);
			}
			
			if (!listRelation.containsKey(entitled3)) {
				if (!entitled3.equals(entitled4)) {
					r1.setInverse(r2);
					r2.setInverse(r1);
					
					listRelation.put(entitled4, r2);
				}
				listRelation.put(entitled3, r1);
			} else {
				r1 = listRelation.get(entitled3);
			}
			
			Triplet triplet = new Triplet();
			triplet.setSSubject(s1);
			triplet.setRelation(r1);
			triplet.setSComplement(s2);
			
			listTriplet.add(triplet);
		}
		
		Subject sCategory = subjectDao.selectSystem("CATEGORY");
		Relation rIsa = relationDao.selectSystem("ISA");
		Relation rInCategory = relationDao.selectSystem("IN CATEGORY");
		
		XPathExpression expr = xpath.compile("/collection/categories/categorie");
		
		NodeList nodeListCategory = (NodeList) expr.evaluate(xml, XPathConstants.NODESET);
		
		for (int i = 0; i < nodeListCategory.getLength(); i++) {
			NodeList nodeListTmpCategory = ((Element) nodeListCategory.item(i)).getElementsByTagName("nom");
			NodeList nodeListTmpMember = ((Element) nodeListCategory.item(i)).getElementsByTagName("membre");
			
			if (nodeListTmpCategory.getLength() != 1) {
				throw new Exception("XML malformé : " + nodeListTmpCategory.getLength());
			}
			
			String entitled = nodeListTmpCategory.item(0).getTextContent();
			
			Subject sNewCategory = new Subject();
			sNewCategory.setEntitled(lang, entitled);
			
			Triplet tCategory = new Triplet();
			tCategory.setSSubject(sNewCategory);
			tCategory.setRelation(rIsa);
			tCategory.setSComplement(sCategory);
			tCategory.setIsSystem(true);
			
			for (int j = 0; j < nodeListTmpMember.getLength(); j++) {
				entitled = nodeListTmpMember.item(j).getTextContent();
				
				Subject s = null;
				
				if (listSubject.containsKey(entitled)) {
					s = listSubject.get(entitled);
				} else {
					s = new Subject();
					s.setEntitled(lang, entitled);
					
					listSubject.put(entitled, s);
				}
				
				Triplet tNewMember = new Triplet();
				tNewMember.setSSubject(s);
				tNewMember.setRelation(rInCategory);
				tNewMember.setSComplement(sNewCategory);
				tNewMember.setIsSystem(true);
				
				listTriplet.add(tNewMember);
			}
			
			listSubject.put(sNewCategory.getEntitled(lang), sNewCategory);
			listTriplet.add(tCategory);
		}
		
		log.info("Count subject : " + listSubject.size());
		log.info("Count relation : " + listRelation.size());
		log.info("Count triplet : " + listTriplet.size());
	}
	
	public void save() throws Exception {
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		RelationDAO relationDao = daoFactory.getRelationDAO();
		TripletDAO tripletDao = daoFactory.getTripletDAO();
		
		Set<String> keys = listSubject.keySet();
		for (String key : keys) {
			List<Subject> listTmp = subjectDao.selectAllByEntitled(key, lang);
			
			Subject newSubject = null;
			
			if (listTmp.size() == 1) {
				newSubject = listTmp.get(0);
			} else if (listTmp.size() == 0) {
				newSubject = subjectDao.add(listSubject.get(key));
			} else {
				log.error("Doublon détecté : " + key);
				continue;
			}
			
			listSubject.put(key, newSubject);
		}
		
		keys = listRelation.keySet();
		for (String key : keys) {
			List<Relation> listTmp = relationDao.selectAllByEntitled(key, lang);
			
			Relation newRelation = null;
			
			if (listTmp.size() == 1) {
				newRelation = listTmp.get(0);
			} else if (listTmp.size() == 0) {
				Relation oldRelation = listRelation.get(key);
				
				if (oldRelation.getInverse() == null) {
					newRelation = relationDao.addSymetrique(oldRelation);
				} else {
					relationDao.addWithInverse(oldRelation);
					
					newRelation = oldRelation;
				}
			} else {
				log.error("Doublon détecté : " + key);
				continue;
			}
			
			listRelation.put(key, newRelation);
		}
		
		for (Triplet triplet : listTriplet) {
			String s1 = triplet.getSSubject().getEntitled(lang);
			String r = triplet.getRelation().getEntitled(lang);
			String s2 = triplet.getSComplement().getEntitled(lang);
			
			if (triplet.getSSubject().getId() == -1) {
				triplet.setSSubject(listSubject.get(s1));
			}
			
			if (triplet.getRelation().getId() == -1) {
				triplet.setRelation(listRelation.get(r));
			}
			
			if (triplet.getSComplement().getId() == -1) {
				triplet.setSComplement(listSubject.get(s2));
			}
			
			boolean isNew = true;
			
			List<Triplet> listTmp = tripletDao.selectAll(triplet.getSSubject().getId(), triplet.getRelation().getId(), triplet.getSComplement().getId(), TripletType.SUBJET, TripletType.SUBJET, false, false);
			listTmp.addAll(tripletDao.selectAll(triplet.getSSubject().getId(), triplet.getRelation().getId(), triplet.getSComplement().getId(), TripletType.SUBJET, TripletType.SUBJET, false, true));
			
			if (listTmp.size() != 0) {
				isNew = false;
			}
			
			if (triplet.getRelation().getInverse() != null) {
				List<Triplet> listTmp2 = tripletDao.selectAll(triplet.getSComplement().getId(), triplet.getRelation().getInverse().getId(), triplet.getSSubject().getId(), TripletType.SUBJET, TripletType.SUBJET, false, false);
				listTmp2.addAll(tripletDao.selectAll(triplet.getSComplement().getId(), triplet.getRelation().getInverse().getId(), triplet.getSSubject().getId(), TripletType.SUBJET, TripletType.SUBJET, false, true));
				
				if (listTmp2.size() != 0) {
					isNew = false;
				}
			}
			
			if (isNew) {
				//log.info("Add Triplet : " + s1 + ", " + r + ", " + s2);
				
				tripletDao.add(triplet);
			}
		}
	}
}