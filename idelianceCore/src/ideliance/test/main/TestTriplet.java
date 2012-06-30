package ideliance.test.main;

import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.RelationDAO;
import ideliance.core.dao.SubjectDAO;
import ideliance.core.dao.TripletDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Relation;
import ideliance.core.object.Subject;
import ideliance.core.object.Triplet;

public class TestTriplet {

	public static void main(String[] args) {
		try {
			peupler();
			select();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public static void peupler() throws DAOException {
		SubjectDAO subjectDao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getSubjectDAO();
		RelationDAO relationDao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getRelationDAO();
		TripletDAO tripletDao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getTripletDAO();
		
		Subject s = new Subject();
		
		s.setEntitled("fr", "Charles");
		s.setFreeText("fr", "Test de sujet");
		
		s = subjectDao.add(s);
		
		Relation r = new Relation();
		
		r.setEntitled("fr", "est une");
		
		r = relationDao.addSymetrique(r);
		
		Subject c = new Subject();
		
		c.setEntitled("fr", "Personne");
		c.setFreeText("fr", "Catégorie");
		
		c = subjectDao.add(c);
		
		Triplet t = new Triplet();
		
		t.setSSubject(s);
		t.setRelation(r);
		t.setSComplement(c);
		
		tripletDao.add(t);
		
		System.out.println(t.toString("fr"));
	}
	
	public static void select() throws DAOException {
		TripletDAO tripletDao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getTripletDAO();
		
		Triplet t = tripletDao.selectSingle(1);
		
		Subject s = t.getSSubject();
		Relation r = t.getRelation();
		Subject c = t.getSComplement();
		
		System.out.println(t.toString("fr"));
		System.out.println(s.getEntitled("fr") + " " + r.getEntitled("fr") + " " + c.getEntitled("fr"));
	}
}
