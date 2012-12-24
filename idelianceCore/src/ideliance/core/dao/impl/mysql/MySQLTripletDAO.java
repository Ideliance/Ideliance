package ideliance.core.dao.impl.mysql;

import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.RelationDAO;
import ideliance.core.dao.SubjectDAO;
import ideliance.core.dao.TripletDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Relation;
import ideliance.core.object.Subject;
import ideliance.core.object.Triplet;
import ideliance.core.object.type.TripletType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class MySQLTripletDAO implements TripletDAO {

	private static final Logger log = Logger.getLogger(MySQLTripletDAO.class);

	private Connection connection = null;
	private DAOFactory daoFactory = null;

	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "ideliance_triplet";

	/**
	 * Table columns
	 */
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_SUBJECT = "subject";
	public static final String COLUMN_RELATION = "relation";
	public static final String COLUMN_COMPLEMENT = "complement";
	public static final String COLUMN_DATE_CREATION = "datecreation";
	public static final String COLUMN_DATE_MODIFICATION = "datemodification";
	public static final String COLUMN_AUTHOR_CREATION = "authorcreation";
	public static final String COLUMN_AUTHOR_MODIFICATION = "authormodification";
	public static final String COLUMN_TYPE_SUBJECT = "typesubject";
	public static final String COLUMN_TYPE_COMPLEMENT = "typecomplement";
	public static final String COLUMN_IS_SYSTEM = "issystem";

	/**
	 * View name
	 */
	public static final String VIEW_NAME = "ideliance_view_triplet";

	/**
	 * View columns
	 */
	public static final String COLUMN_VIEW_LANG = "lang";
	public static final String COLUMN_VIEW_SUBJECT_ENTITLED = "subjectentitled";
	public static final String COLUMN_VIEW_RELATION_ENTITLED = "relationentitled";
	public static final String COLUMN_VIEW_COMPLEMENT_ENTITLED = "complemententitled";

	/**
	 * SQL Requests
	 */
	private static final String REQUEST_INSERT = "INSERT INTO " + TABLE_NAME
			+ " (" + COLUMN_SUBJECT + ", " + COLUMN_RELATION + ", "
			+ COLUMN_COMPLEMENT + ", " + COLUMN_DATE_CREATION + ", "
			+ COLUMN_DATE_MODIFICATION + ", " + COLUMN_AUTHOR_CREATION + ", "
			+ COLUMN_AUTHOR_MODIFICATION + ", " + COLUMN_TYPE_SUBJECT + ", "
			+ COLUMN_TYPE_COMPLEMENT + ", " + COLUMN_IS_SYSTEM
			+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String REQUEST_UPDATE = "UPDATE " + TABLE_NAME
			+ " SET " + COLUMN_SUBJECT + "=?, " + COLUMN_RELATION + "=?, "
			+ COLUMN_COMPLEMENT + "=?, " + COLUMN_DATE_MODIFICATION + "=?, "
			+ COLUMN_AUTHOR_MODIFICATION + "=?, " + COLUMN_TYPE_SUBJECT + "=?,"
			+ COLUMN_TYPE_COMPLEMENT + "=?" + ", " + COLUMN_IS_SYSTEM + "=?"
			+ " WHERE " + COLUMN_ID + "=?";
	private static final String REQUEST_SELECT_UNIQUE = "SELECT * FROM "
			+ TABLE_NAME + " WHERE " + COLUMN_ID + "=?";
	private static final String REQUEST_SELECT_ALL = "SELECT * FROM "
			+ TABLE_NAME + " WHERE isSystem=? ORDER BY " + COLUMN_ID;
	private static final String REQUEST_SELECT_COUNT_ALL = "SELECT COUNT(*) FROM "
			+ TABLE_NAME;
	private static final String REQUEST_SELECT_BY_RELATION = "SELECT * FROM "
			+ TABLE_NAME + " WHERE " + COLUMN_RELATION + "=?";
	private static final String REQUEST_SELECT_BY_SUBJECT_AND_RELATION = "SELECT * FROM "
			+ TABLE_NAME + " WHERE " + COLUMN_SUBJECT + "=? AND " + COLUMN_RELATION + "=?";
	private static final String REQUEST_DELETE = "DELETE FROM " + TABLE_NAME
			+ " WHERE " + COLUMN_ID + "=?";

	public MySQLTripletDAO(Connection connection) throws DAOException {
		this.connection = connection;
		this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
	}

	public Triplet add(Triplet t) throws DAOException {
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_INSERT);
	
			int subjectId = (t.getTypeSubject() == TripletType.SUBJET) ? t.getSSubject().getId() : t.getTSubject().getId();
			int complementId = (t.getTypeComplement() == TripletType.SUBJET) ? t.getSComplement().getId() : t.getTComplement().getId();
	
			pState.setInt(1, subjectId);
			pState.setInt(2, t.getRelation().getId());
			pState.setInt(3, complementId);
			pState.setFloat(4, t.getDateCreation().getTime());
			pState.setFloat(5, t.getDateModification().getTime());
			pState.setString(6, t.getAuthorCreation());
			pState.setString(7, t.getAuthorModification());
			pState.setInt(8, t.getTypeSubject().getValue());
			pState.setInt(9, t.getTypeComplement().getValue());
			pState.setBoolean(10, t.isSystem());
	
			if (log.isDebugEnabled()) {
				log.debug(pState.toString());
			}
	
			// Execute the SQL statement
			pState.execute();
	
			// Get the id of the new triplet
			ResultSet result = pState.getGeneratedKeys();
			if (!result.next()) {
				throw new SQLException("The last insert id was not found");
			}
	
			t.setId(result.getInt(1));
	
			// Close the result and the statement
			try {
				result.close();
			} finally {
				pState.close();
			}
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}

		return t;
	}

	public void update(Triplet t) throws DAOException {
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_UPDATE);
	
			int subjectId = (t.getTypeSubject() == TripletType.SUBJET) ? t.getSSubject().getId() : t.getTSubject().getId();
			int complementId = (t.getTypeComplement() == TripletType.SUBJET) ? t.getSComplement().getId() : t.getTComplement().getId();
	
			pState.setInt(1, subjectId);
			pState.setInt(2, t.getRelation().getId());
			pState.setInt(3, complementId);
			pState.setFloat(4, t.getDateModification().getTime());
			pState.setString(5, t.getAuthorModification());
			pState.setInt(6, t.getTypeSubject().getValue());
			pState.setInt(7, t.getTypeComplement().getValue());
			pState.setBoolean(8, t.isSystem());
			pState.setInt(9, t.getId());
	
			SubjectDAO subjectDao = daoFactory.getSubjectDAO();
			RelationDAO relationDao = daoFactory.getRelationDAO();
	
			// Update Subject - Subject or Triplet
			if (t.getTypeSubject() == TripletType.SUBJET) {
				subjectDao.update(t.getSSubject());
			} else {
				update(t.getTSubject());
			}
	
			// Update Relation
			relationDao.update(t.getRelation());
	
			// Update Complement - Subject or Triplet
			if (t.getTypeComplement() == TripletType.SUBJET) {
				subjectDao.update(t.getSComplement());
			} else {
				update(t.getTComplement());
			}
	
			if (log.isDebugEnabled()) {
				log.debug(pState.toString());
			}
	
			pState.execute();
	
			// Close the statement
			pState.close();
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}
	}

	public Triplet selectSingle(int id) throws DAOException {
		Triplet t = null;
		
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_SELECT_UNIQUE);
	
			pState.setInt(1, id);
	
			if (log.isDebugEnabled()) {
				log.debug(pState.toString());
			}
	
			ResultSet result = pState.executeQuery();
	
			if (result.next()) {
				t = resultToTriplet(result);
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

		return t;
	}

	public List<Triplet> selectAll(boolean isSystem) throws DAOException {
		return selectAll(isSystem, -1, -1);
	}

	public List<Triplet> selectAll(boolean isSystem, int limit) throws DAOException {
		return selectAll(isSystem, limit, -1);
	}

	public List<Triplet> selectAll(boolean isSystem, int limit, int start) throws DAOException {
		List<Triplet> list = new ArrayList<Triplet>();
		
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
	
			// Create a Triplet for each result
			while (result.next()) {
				Triplet t = resultToTriplet(result);
	
				list.add(t);
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
	public int countAll() throws DAOException {
		int count = 0;
		try{
			String query = REQUEST_SELECT_COUNT_ALL;
			// Create the statement
			Statement state = connection.createStatement();
	
			if (log.isDebugEnabled()) {
				log.debug(query);
			}
	
			// Execute the SQL query
			ResultSet result = state.executeQuery(query);
			while (result.next()) {
				count = result.getInt(1);
			}
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}
		return count;
	}
	
	public List<Triplet> selectAllBySubject(int id, TripletType type, boolean inverse) throws DAOException {
		List<Triplet> listTriplet = selectAll(id, -1, -1, type, TripletType.NONE, false, false);
		
		if (inverse == true) {
			listTriplet.addAll(selectAll(-1, -1, id, TripletType.NONE, type, true, false));
		}

		return listTriplet;
	}

	public List<Triplet> selectAllByRelation(int id) throws DAOException {
		List<Triplet> list = new ArrayList<Triplet>();
		
		try {
			// Declare the variables
			PreparedStatement pState;
			ResultSet result;
			
			RelationDAO relationDao = daoFactory.getRelationDAO();
	
			// Select Triplets with this relation
			pState = connection.prepareStatement(REQUEST_SELECT_BY_RELATION);
			pState.setInt(1, id);
	
			// Execute the SQL statement
			result = pState.executeQuery();
	
			// Create a Triplet for each result
			while (result.next()) {
				Triplet t = resultToTriplet(result);
	
				list.add(t);
			}
	
			try {
				result.close();
			} finally {
				pState.close();
			}
	
			// Select Triplets with the inverse relation
			Relation r = relationDao.selectSingle(id).getInverse();
	
			if (r != null) {
				pState = connection.prepareStatement(REQUEST_SELECT_BY_RELATION);
				pState.setInt(1, r.getId());
	
				result = pState.executeQuery();
	
				// Create a Triplet for each result
				while (result.next()) {
					Triplet t = resultToTriplet(result, true);
	
					list.add(t);
				}
	
				try {
					result.close();
				} finally {
					pState.close();
				}
			}
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}

		return list;
	}

	public List<Triplet> selectAllBySubjectAndRelation(int idSubject, int idRelation, TripletType type) throws DAOException {
		List<Triplet> list = new ArrayList<Triplet>();
		
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_SELECT_BY_SUBJECT_AND_RELATION);
			pState.setInt(1, idSubject);
			pState.setInt(2, idRelation);
	
			// Execute the SQL statement
			ResultSet result = pState.executeQuery();
	
			// Create a Triplet for each result
			while (result.next()) {
				Triplet t = resultToTriplet(result);
	
				list.add(t);
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
	
	public List<Triplet> selectAllByRelationAndComplement(int idRelation, int idComplement, TripletType typeComplement, boolean inverse) throws DAOException {
		return selectAll(-1, idRelation, idComplement, TripletType.NONE, typeComplement, inverse, false);
	}
	
	public List<Triplet> selectAll(int idSubject, int idRelation, int idComplement, TripletType typeSubject, TripletType typeComplement, boolean inverse, boolean isSystem) throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("Call selectAll(" + idSubject + ", " + idRelation + ", " + idComplement + ", " + typeSubject + ", " + typeComplement + ", " + inverse + ", " + isSystem + ")");
		}
		
		List<Triplet> listTriplet = new ArrayList<Triplet>();
		
		String query = "SELECT * FROM " + TABLE_NAME;
		
		String where = COLUMN_IS_SYSTEM + "=?";
		
		List<Integer> parameters = new ArrayList<Integer>();
		parameters.add((isSystem) ? 1 : 0);
		
		if (idSubject != -1) {
			where += " AND " + COLUMN_SUBJECT + "=? AND " + COLUMN_TYPE_SUBJECT + "=?";
			
			parameters.add(idSubject);
			parameters.add(typeSubject.getValue());
		}
		
		if (idRelation != -1) {
			where += " AND " + COLUMN_RELATION + "=?";
			
			parameters.add(idRelation);
		}
		
		if (idComplement != -1) {
			where += " AND " + COLUMN_COMPLEMENT + "=? AND " + COLUMN_TYPE_COMPLEMENT + "=?";
			
			parameters.add(idComplement);
			parameters.add(typeComplement.getValue());
		}
		
		query += " WHERE " + where;
		
		try {
			PreparedStatement pState = connection.prepareStatement(query);
			
			for (int i = 1; i <= parameters.size(); i++) {
				pState.setInt(i, parameters.get(i - 1));
			}
			
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
			
			ResultSet result = pState.executeQuery();
	
			while (result.next()) {
				Triplet t = resultToTriplet(result, inverse);
	
				listTriplet.add(t);
			}
	
			try {
				result.close();
			} finally {
				pState.close();
			}
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}
		
		return listTriplet;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>> selectForQuoiEntre(int id) throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("Call simpleSelectAll(" + id + ")");
		}
		
		ArrayList<ArrayList<Integer>> listMatch = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		
		String query = "SELECT CASE WHEN " + COLUMN_SUBJECT + " =? THEN " + COLUMN_COMPLEMENT + " ELSE " + COLUMN_SUBJECT + " END, " + COLUMN_RELATION + ", CASE WHEN " + COLUMN_SUBJECT + " =? THEN 0 ELSE 1 END FROM " + TABLE_NAME;
		// Output : 3 column; 
		//column 1: Subject or complement
		//column 2: relation
		//column 3: 0 if column 1 is complement else 1
		
		String where = COLUMN_SUBJECT + "=? OR " + COLUMN_COMPLEMENT + "=?";
		
		query += " WHERE " + where;

		try {
			PreparedStatement pState = connection.prepareStatement(query);
			
			pState.setInt(1, id);
			pState.setInt(2, id);
			pState.setInt(3, id);
			pState.setInt(4, id);
			
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
			
			ResultSet result = pState.executeQuery();
	
			while (result.next()) {
				tempList.clear();
				tempList.add(result.getInt(1));
				tempList.add(result.getInt(2));
				tempList.add(result.getInt(3));
				//System.out.println(tempList.toString());
				listMatch.add((ArrayList<Integer>) tempList.clone());
				//System.out.println(listMatch.toString());
			}
	
			try {
				result.close();
			} finally {
				pState.close();
			}
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}
		//System.out.println(listMatch.toString());
		return listMatch;
	}

	public void delete(int id) throws DAOException {
		try {
			// Create the statement with the SQL query
			PreparedStatement pState = connection.prepareStatement(REQUEST_DELETE);
	
			pState.setInt(1, id);
	
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
	
	public void deleteBySubject(int idSubject, TripletType typeSubject) throws DAOException {
		deleteByAll(idSubject, -1, -1, typeSubject, TripletType.NONE);
	}
	
	public void deleteByRelation(int idRelation) throws DAOException {
		deleteByAll(-1, idRelation, -1, TripletType.NONE, TripletType.NONE);
	}
	
	public void deleteByComplement(int idComplement, TripletType typeComplement) throws DAOException {
		deleteByAll(-1, -1, idComplement, TripletType.NONE, typeComplement);
	}
	
	public void deleteByAll(int idSubject, int idRelation, int idComplement, TripletType typeSubject, TripletType typeComplement) throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("Call deleteByAll(" + idSubject + ", " + idRelation + ", " + idComplement + ", " + typeSubject + ", " + typeComplement + ")");
		}
		
		String query = "DELETE FROM " + TABLE_NAME;
		
		String where = null;
		
		List<Integer> parameters = new ArrayList<Integer>();
		
		if (idSubject != -1) {
			where = COLUMN_SUBJECT + "=? AND " + COLUMN_TYPE_SUBJECT + "=?";
			
			parameters.add(idSubject);
			parameters.add(typeSubject.getValue());
		}
		
		if (idRelation != -1) {
			where = (where != null) ? where + " AND " : "";
			
			where += COLUMN_RELATION + "=?";
			
			parameters.add(idRelation);
		}
		
		if (idComplement != -1) {
			where = (where != null) ? where + " AND " : "";
			
			where += COLUMN_COMPLEMENT + "=? AND " + COLUMN_TYPE_COMPLEMENT + "=?";
			
			parameters.add(idComplement);
			parameters.add(typeComplement.getValue());
		}
		
		query += " WHERE " + where;
		
		try {
			PreparedStatement pState = connection.prepareStatement(query);
			
			for (int i = 1; i <= parameters.size(); i++) {
				pState.setInt(i, parameters.get(i - 1));
			}
			
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
			
			pState.execute();
			
			pState.close();
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}
	}

	private Triplet resultToTriplet(ResultSet result) throws SQLException, DAOException {
		return resultToTriplet(result, false);
	}

	private Triplet resultToTriplet(ResultSet result, boolean inverse) throws SQLException, DAOException {
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		RelationDAO relationDao = daoFactory.getRelationDAO();

		Triplet t = new Triplet();
		
		t.setId(result.getInt(COLUMN_ID));
		t.setDateCreation(result.getLong(COLUMN_DATE_CREATION));
		t.setDateModification(result.getLong(COLUMN_DATE_MODIFICATION));
		t.setAuthorCreation(result.getString(COLUMN_AUTHOR_CREATION));
		t.setAuthorModification(result.getString(COLUMN_AUTHOR_MODIFICATION));
		t.setIsSystem(result.getBoolean(COLUMN_IS_SYSTEM));

		// Get Subject - Subject or Triplet
		String subjectCol = (inverse == false) ? COLUMN_SUBJECT : COLUMN_COMPLEMENT;
		String subjectTypeCol = (inverse == false) ? COLUMN_TYPE_SUBJECT : COLUMN_TYPE_COMPLEMENT;
		
		if (result.getInt(subjectTypeCol) == TripletType.SUBJET.getValue()) {
			Subject subject = subjectDao.selectSingle(result.getInt(subjectCol));
			t.setSSubject(subject);
		} else {
			Triplet triplet = selectSingle(result.getInt(subjectCol));
			t.setTSubject(triplet);
		}

		// Get Relation
		int relationId = result.getInt(COLUMN_RELATION);

		Relation relation = relationDao.selectSingle(relationId);

		if (inverse == false || relation.getInverse() == null) {
			t.setRelation(relation);
		} else {
			t.setRelation(relation.getInverse());
		}

		// Get Complement - Subject or Triplet
		String complementCol = (inverse == false) ? COLUMN_COMPLEMENT : COLUMN_SUBJECT;
		String complementTypeCol = (inverse == false) ? COLUMN_TYPE_COMPLEMENT : COLUMN_TYPE_SUBJECT;

		if (result.getInt(complementTypeCol) == TripletType.SUBJET.getValue()) {
			Subject subject = subjectDao.selectSingle(result.getInt(complementCol));
			t.setSComplement(subject);
		} else {
			Triplet triplet = selectSingle(result.getInt(complementCol));
			t.setTComplement(triplet);
		}

		return t;
	}
}
