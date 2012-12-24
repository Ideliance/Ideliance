package ideliance.core.dao.impl.mysql;

import ideliance.core.config.ApplicationContext;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.DictionaryDAO;
import ideliance.core.dao.RelationDAO;
import ideliance.core.dao.SubjectDAO;
import ideliance.core.dao.TripletDAO;
import ideliance.core.dao.UserDAO;
import ideliance.core.dao.exception.DAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class MySQLDAOFactory extends DAOFactory {
	
	private static final Logger log = Logger.getLogger(MySQLDAOFactory.class);
	
	private static MySQLDAOFactory _instance = null;
	
	private Connection connection = null;
	
	public static MySQLDAOFactory getInstance() throws DAOException {
		if (_instance == null) {
			_instance = new MySQLDAOFactory();
		}
		
		return _instance;
	}

	private MySQLDAOFactory() throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("Create MySQLDAOFactory - Start");
		}
		
		
		
		if (log.isDebugEnabled()) {
			log.debug("Create MySQLDAOFactory - End");
		}
	}
	
	private Connection getConnection() throws DAOException {
		try {
			if (connection != null && !connection.isClosed()) {
				return connection;
			}
			
			ApplicationContext context = ApplicationContext.getInstance();
			
			Class.forName(context.getProperty("mysql.driver.class"));
	
			String url = context.getProperty("mysql.connection.url");
			String user = context.getProperty("mysql.connection.login");
			String passwd = context.getProperty("mysql.connection.password");
			
			connection = DriverManager.getConnection(url, user, passwd);
			
			if (connection == null) {
				throw new DAOException("Database connection failed (null)");
			}
		} catch (ClassNotFoundException e) {
			throw new DAOException("Driver not found : " + e.getMessage());
		} catch (SQLException e) {
			throw new DAOException("Database connection failed : " + e.getMessage());
		}
		
		return connection;
	}

	@Override
	public DictionaryDAO getDictionaryDAO() throws DAOException {
		return new MySQLDictionaryDAO(getConnection());
	}

	@Override
	public SubjectDAO getSubjectDAO() throws DAOException {
		return new MySQLSubjectDAO(getConnection());
	}

	@Override
	public RelationDAO getRelationDAO() throws DAOException {
		return new MySQLRelationDAO(getConnection());
	}

	@Override
	public TripletDAO getTripletDAO() throws DAOException {
		return new MySQLTripletDAO(getConnection());
	}
	
	@Override
	public UserDAO getUserDAO() throws DAOException {
		return new MySQLUserDAO(getConnection());
	}
}
