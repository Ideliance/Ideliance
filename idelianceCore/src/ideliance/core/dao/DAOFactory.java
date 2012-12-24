package ideliance.core.dao;

import ideliance.core.dao.exception.DAOException;
import ideliance.core.dao.impl.mysql.MySQLDAOFactory;

public abstract class DAOFactory {

	public static final int MYSQL = 1;

	public abstract DictionaryDAO getDictionaryDAO() throws DAOException;

	public abstract SubjectDAO getSubjectDAO() throws DAOException;

	public abstract RelationDAO getRelationDAO() throws DAOException;

	public abstract TripletDAO getTripletDAO() throws DAOException;
	
	public abstract UserDAO getUserDAO() throws DAOException;

	public static DAOFactory getDAOFactory(int factory) throws DAOException {
		switch (factory) {
		case MYSQL:
			return MySQLDAOFactory.getInstance();

		default:
			return null;
		}
	}
}
