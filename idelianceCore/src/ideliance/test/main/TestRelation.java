package ideliance.test.main;

import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.RelationDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Relation;

public class TestRelation {

	public static void main(String [] argv) {
		try {
			peuplerSymetrique();
			peuplerInverse();
			select();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public static void peuplerSymetrique() throws DAOException {
		Relation r1 = new Relation();
		
		r1.setEntitled("fr", "relation fr 1");
		
		RelationDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getRelationDAO();
		
		dao.addSymetrique(r1);
	}
	
	public static void peuplerInverse() throws DAOException {
		Relation r1 = new Relation();
		
		r1.setEntitled("fr", "relation fr 1");

		Relation r2 = new Relation();
		
		r2.setEntitled("fr", "relation inverse");
		
		RelationDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getRelationDAO();
		
		r1.setInverse(r2);
		r2.setInverse(r1);
		
		dao.addWithInverse(r1);
	}
	
	public static void select() throws DAOException {
		RelationDAO dao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getRelationDAO();
		
		Relation r = dao.selectSingle(3);
		
		System.out.println(r);
		System.out.println(r.getInverse());
	}
}
