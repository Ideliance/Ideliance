package ideliance.core.dao;

import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Dictionary;
import ideliance.core.object.type.DictionaryType;

import java.util.Map;

public interface DictionaryDAO {

	/**
	 * Add a Dictionary's instance
	 * 
	 * @param d The instance to add
	 * @throws DAOException
	 */
	public void add(Dictionary d) throws DAOException;
	
	/**
	 * Add several Dictionary's instances with a specific fk 
	 * 
	 * @param fk Id of the associate object (Subject, Relation)
	 * @param map The map of the instances to add
	 * @throws DAOException
	 */
	public void add(int fk, Map<String, Dictionary> map) throws DAOException;
	
	/**
	 * Update a Dictionary's instance
	 * 
	 * @param d The instance to update
	 * @throws DAOException
	 */
	public void update(Dictionary d) throws DAOException;
	
	/**
	 * Update several Dictionary's instances with a specific fk 
	 * 
	 * @param fk Id of the associate object (Subject, Relation)
	 * @param type Type of the instance contains in the map
	 * @param map The map of the instances to update
	 * @throws DAOException
	 */
	public void update(int fk, DictionaryType type, Map<String, Dictionary> map) throws DAOException;
	
//	/**
//	 * Select all the Dictionary's instances by value, lang and type
//	 * 
//	 * @param value Value of Dictionary's instances
//	 * @param lang Lang of Dictionary's instances
//	 * @param type Type of the instances
//	 * @return List which contains the instances
//	 * @throws DAOException
//	 */
//	public List<Dictionary> selectByValue(String value, String lang, DictionaryType type) throws DAOException;
	
	/**
	 * Select all the Dictionary's instances by fk and type
	 * 
	 * @param fk Id of the associate object (Subject, Relation)
	 * @param type Type of the instances
	 * @return Map which contains the instances
	 * @throws DAOException
	 */
	public Map<String, Dictionary> selectByFk(int fk, DictionaryType type) throws DAOException;
	
	/**
	 * Delete a Dictionary's instance
	 * 
	 * @param d The instance to delete
	 * @throws DAOException
	 */
	public void delete(Dictionary d) throws DAOException;
	
	/**
	 * Delete all the Dictionary's instances by fk and type
	 * 
	 * @param fk Id of the associate object (Subject, Relation)
	 * @param type Type of the instances
	 * @throws DAOException
	 */
	public void deleteByFk(int fk, DictionaryType type) throws DAOException;
}