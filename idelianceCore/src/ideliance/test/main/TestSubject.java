package ideliance.test.main;

import ideliance.core.config.MyTimestamp;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.SubjectDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Subject;

public class TestSubject {
	
	public static void main(String[] argv) {
		try {
			peupler();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public static void peupler() throws DAOException {
		Subject s = new Subject();
		
		s.setEntitled("fr", "Test nouvelle liaison");
		s.setEntitled("en", "Test new link");
		
		SubjectDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getSubjectDAO();
		
		s = dao.add(s);
		System.out.println("Avant : " + s);
		
		s.setDateModification(MyTimestamp.getCurrentTimestamp().getTime());
		s.setAuthorModification("Bronx");
		s.setEntitled("fr", "Mise à jour");
		s.setIsSystem(true);
		dao.update(s);
		System.out.println("Après : " + s);
		//dao.delete(s);
		
		s = dao.selectSingle(s.getId());
		System.out.println("Select : " + s);
	}
}
