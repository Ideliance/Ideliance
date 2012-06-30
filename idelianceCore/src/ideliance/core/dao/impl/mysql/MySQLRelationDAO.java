package ideliance.core.dao.impl.mysql;

import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.DictionaryDAO;
import ideliance.core.dao.RelationDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Relation;
import ideliance.core.object.type.DictionaryType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class MySQLRelationDAO implements RelationDAO {

	private static final Logger log = Logger.getLogger(MySQLRelationDAO.class);

	private Connection connection = null;
	private DAOFactory daoFactory = null;

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "ideliance_relation";

	/**
	 * Table columns
	 */
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_INVERSE = "inverse";
	public static final String COLUMN_DATE_CREATION = "datecreation";
	public static final String COLUMN_DATE_MODIFICATION = "datemodification";
	public static final String COLUMN_AUTHOR_CREATION = "authorcreation";
	public static final String COLUMN_AUTHOR_MODIFICATION = "authormodification";
	public static final String COLUMN_IS_SYSTEM = "issystem";

	/**
	 * View name
	 */
	public static final String VIEW_NAME = "ideliance_view_relation";

	/**
	 * View columns
	 */
	public static final String COLUMN_VIEW_LANG = "lang";
	public static final String COLUMN_VIEW_ENTITLED = "entitled";
	public static final String COLUMN_VIEW_INVERSE_ENTITLED = "inverseentitled";

	/**
	 * SQL Requests
	 */
	private static final String REQUEST_INSERT = "INSERT INTO " + TABLE_NAME
			+ " (" + COLUMN_INVERSE + ", "
			+ COLUMN_DATE_CREATION + ", " + COLUMN_DATE_MODIFICATION + ", "
			+ COLUMN_AUTHOR_CREATION + ", " + COLUMN_AUTHOR_MODIFICATION + ", "
			+ COLUMN_IS_SYSTEM + ") VALUES (?, ?, ?, ?, ?, ?)";
	private static final String REQUEST_UPDATE = "UPDATE " + TABLE_NAME
			+ " SET " + COLUMN_INVERSE
			+ "=?, " + COLUMN_DATE_MODIFICATION + "=?, "
			+ COLUMN_AUTHOR_MODIFICATION + "=?" + ", " + COLUMN_IS_SYSTEM
			+ "=?" + " WHERE " + COLUMN_ID + "=?";
	private static final String REQUEST_UPDATE_REFERENCE = "UPDATE "
			+ TABLE_NAME + " SET " + COLUMN_INVERSE + "=? WHERE "
			+ COLUMN_ID + "=?";
	private static final String REQUEST_SELECT_UNIQUE = "SELECT * FROM "
			+ TABLE_NAME + " WHERE " + COLUMN_ID + "=?";
	private static final String REQUEST_SELECT_ALL = "SELECT * FROM "
			+ TABLE_NAME + " WHERE isSystem=? ORDER BY " + COLUMN_ID;
	private static final String REQUEST_SELECT_BY_ENTITLED = "SELECT * FROM "
			+ VIEW_NAME + " WHERE " + COLUMN_VIEW_ENTITLED + "=? AND "
			+ COLUMN_VIEW_LANG + "=?";
	private static final String REQUEST_SEARCH_BY_ENTITLED = "SELECT * FROM "
			+ VIEW_NAME + " WHERE " + COLUMN_VIEW_ENTITLED + " LIKE ? AND "
			+ COLUMN_VIEW_LANG + "=?";
	private static final String REQUEST_SELECT_SYSTEM = "SELECT * FROM "
			+ VIEW_NAME + " WHERE " + COLUMN_VIEW_ENTITLED + "=? AND "
			+ COLUMN_IS_SYSTEM + "=1";
	private static final String REQUEST_DELETE = "DELETE FROM " + TABLE_NAME
			+ " WHERE " + COLUMN_ID + "=?";

	public MySQLRelationDAO(Connection connection) throws DAOException {
		this.connection = connection;
		this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
	}

	public Relation addWithoutInverse(Relation r) throws DAOException {
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_INSERT);
	
			int inverseId = (r.getInverse() != null) ? r.getInverse().getId() : 0;
			
			pState.setInt(1, inverseId);
			pState.setFloat(2, r.getDateCreation().getTime());
			pState.setFloat(3, r.getDateModification().getTime());
			pState.setString(4, r.getAuthorCreation());
			pState.setString(5, r.getAuthorModification());
			pState.setBoolean(6, r.isSystem());
	
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
	
			// Execute the SQL statement
			pState.execute();
	
			// Get the id of the new Relation
			ResultSet result = pState.getGeneratedKeys();
			if (!result.next()) {
				throw new DAOException("The last insert id was not found");
			}
			
			r.setId(result.getInt(1));
	
			// Add Entitled Dictionary fields
			DictionaryDAO dictionaryDao = daoFactory.getDictionaryDAO();
			
			dictionaryDao.add(r.getId(), r.getEntitledMap());
	
			// Close the result and the statement
			try {
				result.close();
			} finally {
				pState.close();
			}
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}
		
		return r;
	}

	public void addWithInverse(Relation r) throws DAOException {
		try {
			// Insert the two relations into the database
			r = addWithoutInverse(r);
			r.setInverse(addWithoutInverse(r.getInverse()));
	
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_UPDATE_REFERENCE);
	
			pState.setInt(1, r.getInverse().getId());
			pState.setInt(2, r.getId());
	
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
	
			// Execute the SQL statement
			pState.execute();
	
			// Close the statement
			pState.close();
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}
	}

	public Relation addSymetrique(Relation r) throws DAOException {
		return addWithoutInverse(r);
	}

	public void update(Relation r) throws DAOException {
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_UPDATE);
	
			int inverseId = (r.getInverse() != null) ? r.getInverse().getId() : 0;
			
			pState.setInt(1, inverseId);
			pState.setFloat(2, r.getDateModification().getTime());
			pState.setString(3, r.getAuthorModification());
			pState.setBoolean(4, r.isSystem());
			pState.setInt(5, r.getId());
	
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
	
			// Execute the SQL query
			pState.execute();
			
			// Update dictionary fields
			DictionaryDAO dictionaryDao = daoFactory.getDictionaryDAO();
			
			dictionaryDao.update(r.getId(), DictionaryType.RELATION_ENTITLED, r.getEntitledMap());
	
			// Close the statement
			pState.close();
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}
	}

	public Relation selectSingle(int id) throws DAOException {
		return selectSingle(id, true);
	}

	public Relation selectSingle(int id, boolean withInverse) throws DAOException {
		Relation r = null;
		
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
				r = resultToRelation(result);
	
				if (withInverse == true && result.getInt(COLUMN_INVERSE) != 0) {
					Relation r2 = selectSingle(result.getInt(COLUMN_INVERSE), false);
					r.setInverse(r2);
					r2.setInverse(r);
				}
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

		return r;
	}
	
	public List<Relation> selectAllByEntitled(String entitled, String lang) throws DAOException {
		return selectAllByEntitled(entitled, lang, true);
	}
	
	public List<Relation> selectAllByEntitled(String entitled, String lang, boolean withInverse) throws DAOException {
		List<Relation> listRelation = new ArrayList<Relation>();
		
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
				Relation r = resultToRelation(result);
	
				if (withInverse == true && result.getInt(COLUMN_INVERSE) != 0) {
					Relation r2 = selectSingle(result.getInt(COLUMN_INVERSE), false);
					r.setInverse(r2);
					r2.setInverse(r);
				}
				
				listRelation.add(r);
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

		return listRelation;
	}
	
	public Relation selectSystem(String entitled) throws DAOException {
		Relation relation = null;
		
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
				relation = resultToRelation(result);
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

		return relation;
	}

	public List<Relation> selectAll(boolean isSystem) throws DAOException {
		return selectAll(isSystem, -1, -1);
	}

	public List<Relation> selectAll(boolean isSystem, int limit) throws DAOException {
		return selectAll(isSystem, limit, -1);
	}

	public List<Relation> selectAll(boolean isSystem, int limit, int start) throws DAOException {
		List<Relation> list = new ArrayList<Relation>();
		
		try {
			String query = REQUEST_SELECT_ALL;
	
			if (limit != -1) {
				query += " LIMIT " + limit;
			}
			if (start != -1) {
				query += " OFFSET " + start;
			}

			// Create the statement
			PreparedStatement pState = connection.prepareStatement(query);

			pState.setBoolean(1, isSystem);
	
			if (log.isDebugEnabled()) {
				log.debug(query);
			}
	
			// Execute the SQL query
			ResultSet result = pState.executeQuery();
	
			// Create a Relation for each result
			while (result.next()) {
				Relation r = resultToRelation(result);
	
				r.setInverse(selectSingle(result.getInt(COLUMN_INVERSE), false));
	
				list.add(r);
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

	public void delete(Relation r) throws DAOException {
		try {
			// Delete dictionary entries
			DictionaryDAO dictionaryDAO = daoFactory.getDictionaryDAO();
	
			dictionaryDAO.deleteByFk(r.getId(), DictionaryType.RELATION_ENTITLED);
	
			// Delete inverse relation
			if (r.getInverse() != null) {
				r.getInverse().setInverse(null);
				delete(r.getInverse());
			}
	
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_DELETE);
	
			pState.setInt(1, r.getId());
	
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
	
	public List<Relation> search(String str, String lang) throws DAOException {
		List<Relation> listRelation = new ArrayList<Relation>();
		
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
				Relation r = resultToRelation(result);
	
				if (result.getInt(COLUMN_INVERSE) != 0) {
					Relation r2 = selectSingle(result.getInt(COLUMN_INVERSE), false);
					r.setInverse(r2);
					r2.setInverse(r);
				}
				
				listRelation.add(r);
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

		return listRelation;
	}

	private Relation resultToRelation(ResultSet result) throws SQLException, DAOException {
		Relation r = new Relation();

		r.setId(result.getInt(COLUMN_ID));
		r.setDateCreation(result.getLong(COLUMN_DATE_CREATION));
		r.setDateModification(result.getLong(COLUMN_DATE_MODIFICATION));
		r.setAuthorCreation(result.getString(COLUMN_AUTHOR_CREATION));
		r.setAuthorModification(result.getString(COLUMN_AUTHOR_MODIFICATION));
		r.setIsSystem(result.getBoolean(COLUMN_IS_SYSTEM));
		
		DictionaryDAO dictionaryDAO = daoFactory.getDictionaryDAO();

		r.setEntitledMap(dictionaryDAO.selectByFk(r.getId(), DictionaryType.RELATION_ENTITLED));

		return r;
	}
}
