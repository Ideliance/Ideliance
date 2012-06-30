package ideliance.core.dao;

import java.util.List;

import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Relation;

public interface RelationDAO {

	/**
	 * Add a Relation's instance without its inverse
	 * 
	 * @param r The instance to add
	 * @return The same instance with its new id
	 * @throws DAOException
	 */
	public Relation addWithoutInverse(Relation r) throws DAOException;
	
	/**
	 * Add a Relation's instance with its inverse
	 * 
	 * @param r The instance to add and its inverse
	 * @throws DAOException
	 */
	public void addWithInverse(Relation r) throws DAOException;
	
	/**
	 * Add a Relation's instance without inverse
	 * 
	 * @param r The instance to add
	 * @return The same instance with its new id
	 * @throws DAOException
	 */
	public Relation addSymetrique(Relation r) throws DAOException;
	
	/**
	 * Update a Relation's instance
	 * 
	 * @param r The instance to update
	 * @throws DAOException
	 */
	public void update(Relation r) throws DAOException;
	
	/**
	 * Select a single Relation's instance by id with its inverse Relation's instance
	 * 
	 * @param id Id of the instance
	 * @return Relation's instance or 'null' if doesn't exists
	 * @throws DAOException
	 */
	public Relation selectSingle(int id) throws DAOException;
	
	/**
	 * Select a single Relation's instance by id with its inverse Relation's instance or not
	 * 
	 * @param id Id of the instance
	 * @param withInverse Select the inverse Relation' instance too?
	 * @return Relation's instance or 'null' if doesn't exists
	 * @throws DAOException
	 */
	public Relation selectSingle(int id, boolean withInverse) throws DAOException;
	
	/**
	 * Select a single Relation's instance by entitled with its inverse Relation's instance
	 * 
	 * @param entitled Entitled of the instance
	 * @return Relation's instance or 'null' if doesn't exists
	 * @throws DAOException
	 */
	public List<Relation> selectAllByEntitled(String entitled, String lang) throws DAOException;
	
	/**
	 * Select a single Relation's instance by entitled with its inverse Relation's instance or not
	 * 
	 * @param entitled Entitled of the instance
	 * @param withInverse Select the inverse Relation' instance too?
	 * @return Relation's instance or 'null' if doesn't exists
	 * @throws DAOException
	 */
	public List<Relation> selectAllByEntitled(String entitled, String lang, boolean withInverse) throws DAOException;
	
	/**
	 * Select a system Relation's instance by entitled
	 * 
	 * @param entitled Entitled of the instance
	 * @return Relation's instance or 'null' if doesn't exists
	 * @throws DAOException
	 */
	public Relation selectSystem(String entitled) throws DAOException;
	
	/**
	 * Select all the Relation's instance
	 * 
	 * @param isSystem Select system's relation or not
	 * @return List of Relation's instance
	 * @throws DAOException
	 */
	public List<Relation> selectAll(boolean isSystem) throws DAOException;
	
	/**
	 * Select all the Relation's instance with a possible limit
	 * 
	 * @param isSystem Select system's relation or not
	 * @param limit Max count of Relation's instance. Put '-1' to select all without limit
	 * @return List of Relation's instance
	 * @throws DAOException
	 */
	public List<Relation> selectAll(boolean isSystem, int limit) throws DAOException;
	
	/**
	 * Select all the Relation's instance with a possible limit and start
	 * 
	 * @param isSystem Select system's relation or not
	 * @param limit Max count of Relation's instance. Put '-1' to select all without limit
	 * @param start Start to this number of Relation's instance. Put '-1' to ignore this parameter
	 * @return List of Relation's instance
	 * @throws DAOException
	 */
	public List<Relation> selectAll(boolean isSystem, int limit, int start) throws DAOException;
	
	/**
	 * Delete the Relation's instance and its inverse 
	 * 
	 * @param r The instance to delete
	 * @throws DAOException
	 */
	public void delete(Relation r) throws DAOException;
	
	/**
	 * Search in BDD all Relation which contains the searched string
	 * 
	 * @param str Searched string
	 * @param lang Search's lang
	 * @return List of Relation's instance
	 * @throws DAOException
	 */
	public List<Relation> search(String str, String lang) throws DAOException;
}