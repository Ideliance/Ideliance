package ideliance.core.dao.impl.mysql;

import ideliance.core.config.ApplicationContext;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.RelationDAO;
import ideliance.core.dao.SubjectDAO;
import ideliance.core.dao.TripletDAO;
import ideliance.core.dao.UserDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Relation;
import ideliance.core.object.Subject;
import ideliance.core.object.Triplet;
import ideliance.core.object.User;
import ideliance.core.object.type.TripletType;
import ideliance.core.object.type.UserLevel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class MySQLUserDAO implements UserDAO {
	private static final Logger log = Logger.getLogger(MySQLUserDAO.class);
	
	private Connection connection = null;
	private DAOFactory daoFactory = null;
	
	/**
	 * View name
	 */
	private static final String VIEW_NAME = "ideliance_view_user";
	
	/**
	 * Columns names
	 */
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_SUBJECT = "subject";
	public static final String COLUMN_SUBJECT_ENTITLED = "subjectentitled";
	public static final String COLUMN_RELATION = "relation";
	public static final String COLUMN_RELATION_ENTITLED = "relationentitled";
	public static final String COLUMN_COMPLEMENT = "complement";
	public static final String COLUMN_COMPLEMENT_ENTITLED = "complemententitled";
	public static final String COLUMN_DATE_CREATION = "datecreation";
	public static final String COLUMN_DATE_MODIFICATION = "datemodification";
	public static final String COLUMN_AUTHOR_CREATION = "authorcreation";
	public static final String COLUMN_AUTHOR_MODIFICATION = "authormodification";
	
	private static final String REQUEST_LOGIN = "SELECT " + COLUMN_SUBJECT + " FROM " + VIEW_NAME
			+ " WHERE " + COLUMN_RELATION_ENTITLED + "='PASSWORD' AND " + COLUMN_SUBJECT_ENTITLED
			+ "=? AND " + COLUMN_COMPLEMENT_ENTITLED + "=?";
	private static final String REQUEST_SELECT_ALL_USER = "SELECT * FROM " + VIEW_NAME + " WHERE " + COLUMN_RELATION_ENTITLED + "='LEVEL'";
	private static final String REQUEST_USER_BY_ID = "SELECT * FROM " + VIEW_NAME + " WHERE " + COLUMN_SUBJECT + "=?";

	public MySQLUserDAO(Connection connection) throws DAOException {
		this.connection = connection;
		daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
	}
	
	public int exists(String login, String password) throws DAOException {
		int idUser = -1;
		
		try {
			PreparedStatement pState = connection.prepareStatement(REQUEST_LOGIN);
	
			pState.setString(1, login);
			pState.setString(2, password);
	
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
			
			ResultSet result = pState.executeQuery();
	
			if (result.next()) {
				idUser = result.getInt(COLUMN_SUBJECT);
			}
	
			result.close();
			pState.close();
		} catch (SQLException e) {
			throw new DAOException("SQLException : " + e.getMessage());
		}

		return idUser;
	}
	
	public List<User> selectAll() throws DAOException {
		List<User> userList = new ArrayList<User>();
		
		try {
			PreparedStatement pState = connection.prepareStatement(REQUEST_SELECT_ALL_USER);
	
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
			
			ResultSet result = pState.executeQuery();
	
			while (result.next()) {
				User u = new User();
				
				u.setId(result.getInt(COLUMN_SUBJECT));
				u.setLogin(result.getString(COLUMN_SUBJECT_ENTITLED));
				u.setLevel(UserLevel.fromString(result.getString(COLUMN_COMPLEMENT_ENTITLED)));
				u.setDateCreation(result.getLong(COLUMN_DATE_CREATION));
				u.setDateModification(result.getLong(COLUMN_DATE_MODIFICATION));
				u.setAuthorCreation(result.getString(COLUMN_AUTHOR_CREATION));
				u.setAuthorModification(result.getString(COLUMN_AUTHOR_MODIFICATION));
				
				userList.add(u);
			}
	
			result.close();
			pState.close();
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}

		return userList;
	}
	
	public User selectSingle(int idUser) throws DAOException {
		User u = null;
		
		try {
			PreparedStatement pState = connection.prepareStatement(REQUEST_USER_BY_ID);
	
			pState.setInt(1, idUser);
	
			if (log.isDebugEnabled()) {
				log.debug(pState.toString());
			}
			
			ResultSet result = pState.executeQuery();
	
			if (result.next()) {
				u = new User();
				
				u.setId(idUser);
				u.setLogin(result.getString(COLUMN_SUBJECT_ENTITLED));
				u.setLevel(getUserLevel(idUser));
				u.setDateCreation(result.getLong(COLUMN_DATE_CREATION));
				u.setDateModification(result.getLong(COLUMN_DATE_MODIFICATION));
				u.setAuthorCreation(result.getString(COLUMN_AUTHOR_CREATION));
				u.setAuthorModification(result.getString(COLUMN_AUTHOR_MODIFICATION));
			}
	
			result.close();
			pState.close();
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}

		return u;
	}
	
	private UserLevel getUserLevel(int idUser) throws DAOException {
		UserLevel level = null;
		
		try {
			String query = REQUEST_USER_BY_ID + " AND " + COLUMN_RELATION_ENTITLED + "='LEVEL'";
			
			PreparedStatement pState = connection.prepareStatement(query);
	
			pState.setInt(1, idUser);
	
			if (log.isDebugEnabled()) {
				log.debug(pState);
			}
			
			ResultSet result = pState.executeQuery();
	
			if (result.next()) {
				level = UserLevel.fromString(result.getString(COLUMN_COMPLEMENT_ENTITLED));
			}
	
			result.close();
			pState.close();
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}

		return level;
	}
	
	public void add(User u) throws DAOException {
		// Get default lang
		String lang = ApplicationContext.getInstance().getProperty("default.dictionary.lang"); 
		
		// Get DAOs
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		RelationDAO relationDao = daoFactory.getRelationDAO();
		TripletDAO tripletDao = daoFactory.getTripletDAO();
		
		// Get system subject
		Subject sUserLevel = subjectDao.selectSystem(u.getLevel().getValue());
		
		// Get systems relations
		Relation rPassword = relationDao.selectSystem("PASSWORD");
		Relation rEst = relationDao.selectSystem("EST");
		
		// Create login subject
		Subject sLogin = new Subject();
		sLogin.setEntitled(lang, u.getLogin());
		
		// Create password subject
		Subject sPassword = new Subject();
		sPassword.setEntitled(lang, u.getPassword());
		
		// Add subjects
		sLogin = subjectDao.add(sLogin);
		sPassword = subjectDao.add(sPassword);
		
		// Create password triplet
		Triplet tPassword = new Triplet();
		tPassword.setSSubject(sLogin);
		tPassword.setRelation(rPassword);
		tPassword.setSComplement(sPassword);
		tPassword.setIsSystem(true);
		
		// Create level triplet
		Triplet tLevel = new Triplet();
		tLevel.setSSubject(sLogin);
		tLevel.setRelation(rEst);
		tLevel.setSComplement(sUserLevel);
		tLevel.setIsSystem(true);
		
		// Add triplets
		tripletDao.add(tPassword);
		tripletDao.add(tLevel);
	}
	
	public void update(User u) throws DAOException {
		// Get default lang
		String lang = ApplicationContext.getInstance().getProperty("default.dictionary.lang"); 
		
		// Get DAOs
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		RelationDAO relationDao = daoFactory.getRelationDAO();
		TripletDAO tripletDao = daoFactory.getTripletDAO();
		
		// Select 'PASSWORD' relation
		Relation rPassword = relationDao.selectSystem("PASSWORD");
		
		// Find password subject
		List<Triplet> tripletList = tripletDao.selectAllBySubjectAndRelation(u.getId(), rPassword.getId(), TripletType.SUBJET);
		Triplet tPassword = tripletList.get(0);
		
		// Select subjects
		Subject sLogin = subjectDao.selectSingle(u.getId());
		Subject sPassword = subjectDao.selectSingle(tPassword.getSComplement().getId());
		
		if (!sLogin.getEntitled(lang).equals(u.getLogin())) {
			sLogin.setEntitled(lang, u.getLogin());
			
			subjectDao.update(sLogin);
		}
		
		if (!sPassword.getEntitled(lang).equals(u.getPassword())) {
			sPassword.setEntitled(lang, u.getPassword());
			
			subjectDao.update(sPassword);
		}
	}
	
	public void delete(int id) throws DAOException {
		// Get DAOs
		SubjectDAO subjectDao = daoFactory.getSubjectDAO();
		RelationDAO relationDao = daoFactory.getRelationDAO();
		TripletDAO tripletDao = daoFactory.getTripletDAO();
		
		// Select 'PASSWORD' relation
		Relation rPassword = relationDao.selectSystem("PASSWORD");
		
		// Find password subject
		List<Triplet> tripletList = tripletDao.selectAllBySubjectAndRelation(id, rPassword.getId(), TripletType.SUBJET);
		Triplet tPassword = tripletList.get(0);
		
		// Delete triplets
		tripletDao.deleteBySubject(id, TripletType.SUBJET);
		
		// Delete subjects
		subjectDao.delete(id);
		subjectDao.delete(tPassword.getSComplement().getId());
	}
}
