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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportSVC {
//	private static final Logger log = Logger.getLogger(ImportSVC.class);
	
	private DAOFactory daoFactory = null;
	
	public ImportSVC() throws DAOException {
		ApplicationContext context = ApplicationContext.getInstance();
		
		daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
	}
	
	public boolean parseSVCFile(String path) throws DAOException {
		Map<String, Subject> subjects = new HashMap<String, Subject>();
		Map<String, Relation> relations = new HashMap<String, Relation>();
		Map<String, Triplet> triplets = new HashMap<String, Triplet>();
		
		try {
			InputStream input = new FileInputStream(path);
			InputStreamReader reader = new InputStreamReader(input);
			BufferedReader buffer = new BufferedReader(reader);
			
			String line = "";
			while ((line = buffer.readLine()) != null) {
				if (line.matches("_S .* = relation")) {
					String relation = line.replace("_S ", "").replace(" = relation", "");
					
					if (relations.get(relation) == null) {
						String [] rel = relation.split(",");
						
						Relation r = new Relation();
						r.setEntitled("fr", rel[0]);
						r.setAuthorCreation("Import");
						r.setAuthorModification("Import");
						
						if (!rel[0].equals(rel[1])) {
							Relation r2 = new Relation();
							r2.setEntitled("fr", rel[1]);
							r2.setAuthorCreation("Import");
							r2.setAuthorModification("Import");
							
							r.setInverse(r2);
							r2.setInverse(r);
							
							relations.put(rel[1], r2);
						}
						
						relations.put(rel[0], r);
					}
				} else if (line.matches("_S .* = Catégorie")) {
					if (relations.get("est une") == null) {
						Relation r = new Relation();
						r.setEntitled("fr", "est une");
						r.setAuthorCreation("Import");
						r.setAuthorModification("Import");
						
						relations.put("est une", r);
					}
					
					if (subjects.get("Catégorie") == null) {
						Subject s = new Subject();
						s.setEntitled("fr", "Catégorie");
						s.setAuthorCreation("Import");
						s.setAuthorModification("Import");
						
						subjects.put("Catégorie", s);
					}
					
					String categorie = line.replace("_S ", "").replace(" = Catégorie", "");
					
					Subject s = new Subject();
					s.setEntitled("fr", categorie);
					s.setAuthorCreation("Import");
					s.setAuthorModification("Import");
					
					subjects.put(categorie, s);
					
					Triplet t = new Triplet();
					t.setSSubject(s);
					t.setRelation(relations.get("est une"));
					t.setSComplement(subjects.get("Catégorie"));
					t.setAuthorCreation("Import");
					t.setAuthorModification("Import");
					
					triplets.put(t.toString("fr"), t);
				} else if (line.matches("_S .* = .*")) {
					if (relations.get("appartient à la catégorie") == null) {
						Relation r1 = new Relation();
						r1.setEntitled("fr", "appartient à la catégorie");
						r1.setAuthorCreation("Import");
						r1.setAuthorModification("Import");
						
						Relation r2 = new Relation();
						r2.setEntitled("fr", "contient le sujet");
						r2.setAuthorCreation("Import");
						r2.setAuthorModification("Import");
						
						r1.setInverse(r2);
						r2.setInverse(r1);
						
						relations.put("appartient à la catégorie", r1);
						relations.put("contient le sujet", r2);
					}
					
					Pattern pattern = Pattern.compile("_S (.*) = (.*)");
					Matcher matcher = pattern.matcher(line);
					
					matcher.find();
					
					String subject1 = matcher.group(1);
					String subject2 = matcher.group(2);
					
					if (subjects.get(subject1) == null) {
						Subject s = new Subject();
						s.setEntitled("fr", subject1);
						s.setAuthorCreation("Import");
						s.setAuthorModification("Import");
						
						subjects.put(subject1, s);
					}
					
					if (subjects.get(subject2) == null) {
						Subject s = new Subject();
						s.setEntitled("fr", subject2);
						s.setAuthorCreation("Import");
						s.setAuthorModification("Import");
						
						subjects.put(subject2, s);
					}
					
					Triplet t = new Triplet();
					t.setSSubject(subjects.get(subject1));
					t.setRelation(relations.get("appartient à la catégorie"));
					t.setSComplement(subjects.get(subject2));
					t.setAuthorCreation("Import");
					t.setAuthorModification("Import");
					
					triplets.put(t.toString("fr"), t);
				} else if (line.matches("_S .* _V <Texte> _C .*")) {
					Pattern pattern = Pattern.compile("_S (.*) _V <Texte> _C (.*)");
					Matcher matcher = pattern.matcher(line);
					
					matcher.find();
					
					String subject = matcher.group(1);
					String text = matcher.group(2);
					
					Subject s = subjects.get(subject);
					
					String newText = s.getFreeText("fr") + "\n" + text;
					s.setFreeText("fr", newText);
				} else if (line.matches("_S .* _V .* _C .*")) {
					Pattern pattern = Pattern.compile("_S (.*) _V (.*) _C (.*)");
					Matcher matcher = pattern.matcher(line);
					
					matcher.find();
					
					String subject = matcher.group(1);
					String relation = matcher.group(2);
					String complement = matcher.group(3);
					
					Subject s1 = subjects.get(subject);
					Relation r = relations.get(relation);
					Subject s2 = subjects.get(complement);
					if (s1 != null && r != null && s2 != null) {
						Triplet t = new Triplet();
						t.setSSubject(s1);
						t.setRelation(r);
						t.setSComplement(s2);
						t.setAuthorCreation("Import");
						t.setAuthorModification("Import");
						
						triplets.put(t.toString("fr"), t);
					} else {
						//System.err.println(subject + " " + relation + " " + complement);
					}
				}
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String, Subject> newSubjects = new HashMap<String, Subject>();
		Map<String, Relation> newRelations = new HashMap<String, Relation>();
//		Map<String, Triplet> newTriplets = new HashMap<String, Triplet>();
		
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		RelationDAO relationDao = daoFactory.getRelationDAO();
		TripletDAO tripletDao = daoFactory.getTripletDAO();
		
		Set<String> subjectKeys = subjects.keySet();
		for (String key : subjectKeys) {
			Subject s = subjects.get(key);
			
			s = subjectDao.add(s);
			
			newSubjects.put(key, s);
		}
		
		Set<String> relationKeys = relations.keySet();
		for (String key : relationKeys) {
			Relation r1 = relations.get(key);
			
			if (newRelations.get(key) == null && r1.getInverse() == null) {
				r1 = relationDao.addSymetrique(r1);
				
				newRelations.put(key, r1);
			} else if (newRelations.get(key) == null && newRelations.get(r1.getInverse().getEntitled("fr")) == null) {
				Relation r2 = relations.get(r1.getInverse().getEntitled("fr"));
				
				r1.setInverse(r2);
				r2.setInverse(r1);
				
				relationDao.addWithInverse(r1);
				
				newRelations.put(key, r1);
				newRelations.put(r2.getEntitled("fr"), r2);
			}
		}
		
		Set<String> keys = triplets.keySet();
		for (String key : keys) {
			Triplet t = triplets.get(key);
			
			Subject s1 = subjects.get(t.getSSubject().getEntitled("fr"));
			Relation r = relations.get(t.getRelation().getEntitled("fr"));
			Subject s2 = subjects.get(t.getSComplement().getEntitled("fr"));
			
			t.setSSubject(s1);
			t.setRelation(r);
			t.setSComplement(s2);
			
			tripletDao.add(t);
		}
		
		return true;
	}
}