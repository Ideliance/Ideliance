package ideliance.core.dao.impl.mysql;

import ideliance.core.dao.DictionaryDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Dictionary;
import ideliance.core.object.type.DictionaryType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class MySQLDictionaryDAO implements DictionaryDAO {

	private static final Logger log = Logger.getLogger(MySQLDictionaryDAO.class);

	private Connection connection = null;

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "ideliance_dictionary";

	/**
	 * Table columns
	 */
	public static final String COLUMN_FK = "fk";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_LANG = "lang";
	public static final String COLUMN_VALUE = "value";

	/**
	 * SQL Requests
	 */
	private static final String REQUEST_INSERT = "INSERT INTO " + TABLE_NAME
			+ " (" + COLUMN_FK + ", " + COLUMN_TYPE + ", " + COLUMN_LANG + ", " + COLUMN_VALUE
			+ ") VALUES (?, ?, ?, ?)";
	private static final String REQUEST_UPDATE = "UPDATE " + TABLE_NAME + " SET "
			+ COLUMN_VALUE + "=? WHERE " + COLUMN_FK + "=? AND " + COLUMN_TYPE + "=? AND "
			+ COLUMN_LANG + "=?";
//	private static final String REQUEST_SELECT_BY_VALUE = "SELECT * FROM " + TABLE_NAME
//			+ " WHERE " + COLUMN_TYPE + "=? AND " + COLUMN_LANG + "=? AND "
//			+ COLUMN_VALUE + "=?";
	private static final String REQUEST_SELECT_BY_FK = "SELECT * FROM "
			+ TABLE_NAME + " WHERE " + COLUMN_FK + "=? AND " + COLUMN_TYPE + "=?";
	private static final String REQUEST_DELETE = "DELETE FROM " + TABLE_NAME
			+ " WHERE " + COLUMN_FK + "=? AND " + COLUMN_TYPE + "=? AND "
			+ COLUMN_LANG + "=?";
	private static final String REQUEST_DELETE_BY_FK = "DELETE FROM "
			+ TABLE_NAME + " WHERE " + COLUMN_FK + "=? AND " + COLUMN_TYPE + "=?";

	public MySQLDictionaryDAO(Connection connection) {
		this.connection = connection;
	}

	public void add(Dictionary d) throws DAOException {
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_INSERT);
	
			// Put the parameters in the prepared statement
			pState.setInt(1, d.getFk());
			pState.setInt(2, d.getType().getValue());
			pState.setString(3, d.getLang());
			pState.setString(4, d.getValue());
	
			if (log.isDebugEnabled()) {
				log.debug("Query : " + pState);
			}
	
			// Execute the SQL statement
			pState.execute();
			
			// Close the statement
			pState.close();
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}
	}

	public void add(int fk, Map<String, Dictionary> map) throws DAOException {
		
		// Add each instance of Dictionary in the map
		Set<String> keys = map.keySet();
		for (String key : keys) {
			Dictionary d = map.get(key);
			d.setFk(fk);
			
			add(d);
		}
	}
	
	public void update(Dictionary d) throws DAOException {
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_UPDATE);
	
			pState.setString(1, d.getValue());
			pState.setInt(2, d.getFk());
			pState.setInt(3, d.getType().getValue());
			pState.setString(4, d.getLang());
	
			if (log.isDebugEnabled()) {
				log.debug(pState.toString());
			}
	
			// Execute the SQL statement
			pState.execute();
	
			// Close the statement
			pState.close();
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}
	}

	// TODO Change the update functions for Dictionary DAO
	public void update(int fk, DictionaryType type, Map<String, Dictionary> map) throws DAOException {
		deleteByFk(fk, type);

		add(fk, map);
	}
	
//	public List<Dictionary> selectByValue(String value, String lang, DictionaryType type) throws DAOException {
//		List<Dictionary> listDictionary = new ArrayList<Dictionary>();
//		
//		try {
//			// Create the statement with the SQL query
//			PreparedStatement pState = connection.prepareStatement(REQUEST_SELECT_BY_VALUE);
//	
//			pState.setInt(1, type.getValue());
//			pState.setString(2, lang);
//			pState.setString(3, value);
//	
//			if (log.isDebugEnabled()) {
//				log.debug(pState.toString());
//			}
//	
//			// Execute the SQL statement
//			ResultSet result = pState.executeQuery();
//	
//			// Put each result in the map
//			while (result.next()) {
//				Dictionary d = resultToDictionary(result);
//	
//				listDictionary.add(d);
//			}
//	
//			// Close the result and the statement
//			try {
//				result.close();
//			} finally {
//				pState.close();
//			}
//		} catch (SQLException e) {
//			throw new DAOException("SQLException : " + e.getMessage());
//		}
//		
//		return listDictionary;
//	}
	
	public Map<String, Dictionary> selectByFk(int fk, DictionaryType type) throws DAOException {
		Map<String, Dictionary> map = new HashMap<String, Dictionary>();
		
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_SELECT_BY_FK);
	
			pState.setInt(1, fk);
			pState.setInt(2, type.getValue());
	
			if (log.isDebugEnabled()) {
				log.debug(pState.toString());
			}
	
			// Execute the SQL statement
			ResultSet result = pState.executeQuery();
	
			// Put each result in the map
			while (result.next()) {
				Dictionary d = resultToDictionary(result);
	
				map.put(d.getLang(), d);
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

		return map;
	}

	public void delete(Dictionary d) throws DAOException {
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_DELETE);
	
			pState.setInt(1, d.getFk());
			pState.setInt(2, d.getType().getValue());
			pState.setString(3, d.getLang());
	
			if (log.isDebugEnabled()) {
				log.debug(pState.toString());
			}
	
			// Execute the SQL statement
			pState.execute();
			
			// Close the statement
			pState.close();
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}
	}

	public void deleteByFk(int fk, DictionaryType type) throws DAOException {
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_DELETE_BY_FK);
	
			pState.setInt(1, fk);
			pState.setInt(2, type.getValue());
	
			if (log.isDebugEnabled()) {
				log.debug(pState.toString());
			}
	
			// Execute the SQL statement
			pState.execute();
			
			// Close the statement
			pState.close();
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}
	}

	private Dictionary resultToDictionary(ResultSet result) throws SQLException {
		Dictionary d = new Dictionary();

		d.setFk(result.getInt(COLUMN_FK));
		d.setType(DictionaryType.fromInt(result.getInt(COLUMN_TYPE)));
		d.setLang(result.getString(COLUMN_LANG));
		d.setValue(result.getString(COLUMN_VALUE));

		return d;
	}
}
