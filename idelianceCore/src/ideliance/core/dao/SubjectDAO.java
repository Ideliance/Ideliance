package ideliance.core.dao;

import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Subject;

import java.util.List;

public interface SubjectDAO {

	/**
	 * Add a Subject's instance
	 * 
	 * @param s The instance to add
	 * @return The same instance with its new id
	 * @throws DAOException
	 */
	public Subject add(Subject s) throws DAOException;
	
	/**
	 * Update a Subject's instance
	 * 
	 * @param s The instance to update
	 * @throws DAOException
	 */
	public void update(Subject s) throws DAOException;
	
	/**
	 * Select a single Subject's instance by id
	 * 
	 * @param id Id of the instance
	 * @return Subject's instance or 'null' if doesn't exists
	 * @throws DAOException
	 */
	public Subject selectSingle(int id) throws DAOException;
	
	/**
	 * Select a single Subject's instance by entitled
	 * 
	 * @param entitled Entitled of the instance
	 * @param lang Lang of the parameter entitled
	 * @return List of Subject's instance
	 * @throws DAOException
	 */
	public List<Subject> selectAllByEntitled(String entitled, String lang) throws DAOException;
	
	/**
	 * Select a system Subject's instance by entitled
	 * 
	 * @param entitled Entitled of the instance
	 * @return Subject's instance or 'null' if doesn't exists
	 * @throws DAOException
	 */
	public Subject selectSystem(String entitled) throws DAOException;
	
	/**
	 * Select all the Subject's instance
	 * 
	 * @param isSystem Select system's subject or not
	 * @param lang The lang to select the Dictionary's instance
	 * @return List of Subject's instance
	 * @throws DAOException
	 */
	public List<Subject> selectAll(boolean isSystem) throws DAOException;
	
	/**
	 * Select all the Subject's instance with a possible limit
	 * 
	 * @param isSystem Select system's subject or not
	 * @param limit Max count of Subject's instance. Put '-1' to select all without limit
	 * @param lang The lang to select the Dictionary's instance
	 * @return List of Subject's instance
	 * @throws DAOException
	 */
	public List<Subject> selectAll(boolean isSystem, int limit) throws DAOException;
	
	/**
	 * Select all the Relation's instance with a possible limit and start
	 * 
	 * @param isSystem Select system's subject or not
	 * @param limit Max count of Subject's instance. Put '-1' to select all without limit
	 * @param start Start to this number of Subject's instance. Put '-1' to ignore this parameter
	 * @param lang The lang to select the Dictionary's instance
	 * @return List of Subject's instance
	 * @throws DAOException
	 */
	public List<Subject> selectAll(boolean isSystem, int limit, int start) throws DAOException;
	
	/**
	 * Delete the Subject's instance by id
	 * 
	 * @param id Id of the Subject's intance to delete
	 * @throws DAOException
	 */
	public void delete(int id) throws DAOException;
	
	/**
	 * Search in BDD all Subject which contains the searched string
	 * 
	 * @param str Searched string
	 * @param lang Search's lang
	 * @return List of Subject's instance
	 * @throws DAOException
	 */
	public List<Subject> search(String str, String lang) throws DAOException;
}