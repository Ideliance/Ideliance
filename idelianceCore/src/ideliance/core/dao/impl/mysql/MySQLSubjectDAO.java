package ideliance.core.dao.impl.mysql;

import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.DictionaryDAO;
import ideliance.core.dao.SubjectDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Subject;
import ideliance.core.object.type.DictionaryType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class MySQLSubjectDAO implements SubjectDAO {

	private static final Logger log = Logger.getLogger(MySQLSubjectDAO.class);

	private Connection connection = null;
	private DAOFactory daoFactory = null;

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "ideliance_subject";

	/**
	 * Table columns
	 */
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_DATE_CREATION = "datecreation";
	public static final String COLUMN_DATE_MODIFICATION = "datemodification";
	public static final String COLUMN_AUTHOR_CREATION = "authorcreation";
	public static final String COLUMN_AUTHOR_MODIFICATION = "authormodification";
	public static final String COLUMN_IS_SYSTEM = "issystem";
	
	/**
	 * View name
	 */
	public static final String VIEW_NAME = "ideliance_view_subject";
	
	/**
	 * View columns
	 */
	public static final String COLUMN_VIEW_LANG = "lang";
	public static final String COLUMN_VIEW_ENTITLED = "entitled";
	public static final String COLUMN_VIEW_FREE_TEXT = "freetext";

	/**
	 * SQL Requests
	 */
	private static final String REQUEST_INSERT = "INSERT INTO " + TABLE_NAME + " ("
			+ COLUMN_DATE_CREATION + ", " + COLUMN_DATE_MODIFICATION + ", "
			+ COLUMN_AUTHOR_CREATION + ", " + COLUMN_AUTHOR_MODIFICATION + ", "
			+ COLUMN_IS_SYSTEM + ") VALUES (?, ?, ?, ?, ?)";
	private static final String REQUEST_UPDATE = "UPDATE " + TABLE_NAME + " SET "
			+ COLUMN_DATE_MODIFICATION + "=?, " + COLUMN_AUTHOR_MODIFICATION
			+ "=?" + ", " + COLUMN_IS_SYSTEM + "=?" + " WHERE " + COLUMN_ID + "=?";
	private static final String REQUEST_SELECT_UNIQUE = "SELECT * FROM "
			+ VIEW_NAME + " WHERE " + COLUMN_ID + "=?";
	private static final String REQUEST_SELECT_ALL = "SELECT * FROM "
			+ VIEW_NAME + " WHERE issystem=? ORDER BY " + COLUMN_ID;
	private static final String REQUEST_SELECT_BY_ENTITLED = "SELECT * FROM "
			+ VIEW_NAME + " WHERE " + COLUMN_VIEW_ENTITLED + "=? AND " + COLUMN_VIEW_LANG + "=?";
	private static final String REQUEST_SEARCH_BY_ENTITLED = "SELECT * FROM "
			+ VIEW_NAME + " WHERE " + COLUMN_VIEW_ENTITLED + " LIKE ? AND " + COLUMN_VIEW_LANG + "=?";
	private static final String REQUEST_SELECT_SYSTEM = "SELECT * FROM "
			+ VIEW_NAME + " WHERE " + COLUMN_VIEW_ENTITLED + "=? AND " + COLUMN_IS_SYSTEM + "=1";
	private static final String REQUEST_DELETE = "DELETE FROM " + TABLE_NAME
			+ " WHERE " + COLUMN_ID + "=?";

	public MySQLSubjectDAO(Connection connection) throws DAOException {
		this.connection = connection;
		this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
	}

	public Subject add(Subject s) throws DAOException {
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_INSERT);
	
			pState.setFloat(1, s.getDateCreation().getTime());
			pState.setFloat(2, s.getDateModification().getTime());
			pState.setString(3, s.getAuthorCreation());
			pState.setString(4, s.getAuthorModification());
			pState.setBoolean(5, s.isSystem());
			
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
	
			// Execute the SQL statement
			pState.execute();
			
			// Get the id of the new subject
			ResultSet result = pState.getGeneratedKeys();
			if (!result.next()) {
				throw new SQLException("The last insert id was not found");
			}
			
			s.setId(result.getInt(1));
			
			// Update dictionary
			DictionaryDAO dictionaryDAO = daoFactory.getDictionaryDAO();
	
			dictionaryDAO.add(s.getId(), s.getEntitledMap());
			dictionaryDAO.add(s.getId(), s.getFreeTextMap());
	
			// Close the result and the statement
			try {
				result.close();
			} finally {
				pState.close();
			}
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}

		return s;
	}

	public void update(Subject s) throws DAOException {
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_UPDATE);
	
			pState.setFloat(1, s.getDateModification().getTime());
			pState.setString(2, s.getAuthorModification());
			pState.setBoolean(3, s.isSystem());
			pState.setInt(4, s.getId());
	
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
	
			// Execute the SQL statement
			pState.execute();
			
			// Update dictionary fields
			DictionaryDAO dictionaryDAO = daoFactory.getDictionaryDAO();
	
			dictionaryDAO.update(s.getId(), DictionaryType.SUBJECT_ENTITLED, s.getEntitledMap());
	
			dictionaryDAO.update(s.getId(), DictionaryType.SUBJECT_FREE_TEXT, s.getFreeTextMap());
	
			// Close the statement
			pState.close();
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}
	}

	public Subject selectSingle(int id) throws DAOException {
		Subject s = null;
		
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_SELECT_UNIQUE);
	
			pState.setInt(1, id);
	
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
	
			// Execute the SQL statement
			ResultSet result = pState.executeQuery();
	
			if (result.next()) {
				s = resultToSubject(result);
			}
	
			// Close the result and the statement
			try {
				result.close();
			} finally {
				pState.close();
			}
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}

		return s;
	}

	@Override
	public List<Subject> selectAllByEntitled(String entitled, String lang) throws DAOException {
		List<Subject> listSubject = new ArrayList<Subject>();
		
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_SELECT_BY_ENTITLED);
	
			pState.setString(1, entitled);
			pState.setString(2, lang);
	
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
	
			// Execute the SQL statement
			ResultSet result = pState.executeQuery();
	
			while (result.next()) {
				Subject s = resultToSubject(result);
				
				listSubject.add(s);
			}

			// Close the result and the statement
			try {
				result.close();
			} finally {
				pState.close();
			}
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}

		return listSubject;
	}

	@Override
	public Subject selectSystem(String entitled) throws DAOException {
		Subject subject = null;
		
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_SELECT_SYSTEM);
	
			pState.setString(1, entitled);
	
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
	
			// Execute the SQL statement
			ResultSet result = pState.executeQuery();
	
			if (result.next()) {
				subject = resultToSubject(result);
			}

			// Close the result and the statement
			try {
				result.close();
			} finally {
				pState.close();
			}
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}

		return subject;
	}

	public List<Subject> selectAll(boolean isSystem) throws DAOException {
		return selectAll(isSystem, -1, -1);
	}

	public List<Subject> selectAll(boolean isSystem, int limit) throws DAOException {
		return selectAll(isSystem, limit, -1);
	}

	public List<Subject> selectAll(boolean isSystem, int limit, int start) throws DAOException {
		List<Subject> list = new ArrayList<Subject>();
		
		try {
			String query = REQUEST_SELECT_ALL;
	
			if (limit != -1) {
				query += " LIMIT " + limit;
			}
			if (start != -1) {
				query += " OFFSET " + start;
			}
	
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(query);

			pState.setBoolean(1, isSystem);
			
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
	
			// Execute the SQL statement
			ResultSet result = pState.executeQuery();
	
			// Create a Subject for each result
			while (result.next()) {
				Subject s = resultToSubject(result);
	
				list.add(s);
			}
	
			// Close the result and the statement
			try {
				result.close();
			} finally {
				pState.close();
			}
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}

		return list;
	}

	public void delete(int id) throws DAOException {
		try {
			// Delete dictionary entries
			DictionaryDAO dictionaryDAO = daoFactory.getDictionaryDAO();
	
			dictionaryDAO.deleteByFk(id, DictionaryType.SUBJECT_ENTITLED);
			dictionaryDAO.deleteByFk(id, DictionaryType.SUBJECT_FREE_TEXT);
	
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_DELETE);
	
			pState.setInt(1, id);
	
			if (log.isDebugEnabled()) {
				log.debug(REQUEST_DELETE);
			}
	
			// Execute the SQL statement
			pState.execute();
	
			// Close the statement
			pState.close();
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}
	}
	
	public List<Subject> search(String str, String lang) throws DAOException {
		List<Subject> listSubject = new ArrayList<Subject>();
		
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_SEARCH_BY_ENTITLED);
	
			pState.setString(1, "%" + str + "%");
			pState.setString(2, lang);
	
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
	
			// Execute the SQL statement
			ResultSet result = pState.executeQuery();
	
			while (result.next()) {
				Subject s = resultToSubject(result);
				
				listSubject.add(s);
			}

			// Close the result and the statement
			try {
				result.close();
			} finally {
				pState.close();
			}
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}

		return listSubject;
	}

	private Subject resultToSubject(ResultSet result) throws SQLException, DAOException {
		Subject s = new Subject();

		s.setId(result.getInt(COLUMN_ID));
		s.setDateCreation(result.getLong(COLUMN_DATE_CREATION));
		s.setDateModification(result.getLong(COLUMN_DATE_MODIFICATION));
		s.setAuthorCreation(result.getString(COLUMN_AUTHOR_CREATION));
		s.setAuthorModification(result.getString(COLUMN_AUTHOR_MODIFICATION));
		s.setIsSystem(result.getBoolean(COLUMN_IS_SYSTEM));
		
		DictionaryDAO dictionaryDao = daoFactory.getDictionaryDAO();
		
		s.setEntitledMap(dictionaryDao.selectByFk(s.getId(), DictionaryType.SUBJECT_ENTITLED));
		s.setFreeTextMap(dictionaryDao.selectByFk(s.getId(), DictionaryType.SUBJECT_FREE_TEXT));

		return s;
	}
}
