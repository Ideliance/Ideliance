package ideliance.core.dao;

import java.util.ArrayList;
import java.util.List;

import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Triplet;
import ideliance.core.object.type.TripletType;

public interface TripletDAO {

	/**
	 * Add a Triplet's instance
	 * 
	 * @param t The instance to add
	 * @return The same instance with its new id
	 * @throws DAOException
	 */
	public Triplet add(Triplet t) throws DAOException;
	
	/**
	 * Update a Triplet's instance
	 * 
	 * @param t The instance to update
	 * @throws DAOException
	 */
	public void update(Triplet t) throws DAOException;
	
	/**
	 * Select a single Triplet's instance by id
	 * 
	 * @param id Id of the instance
	 * @return Triplet's instance or 'null' if doesn't exists
	 * @throws DAOException
	 */
	public Triplet selectSingle(int id) throws DAOException;
	
	/**
	 * Select all the Triplet's instance
	 * 
	 * @param isSystem Select system's subject or not
	 * @return List of Triplet's instance
	 * @throws DAOException
	 */
	public List<Triplet> selectAll(boolean isSystem) throws DAOException;
	
	/**
	 * Select all the Triplet's instance with a possible limit
	 * 
	 * @param isSystem Select system's subject or not
	 * @param limit Max count of Triplet's instance. Put '-1' to select all without limit
	 * @return List of Triplet's instance
	 * @throws DAOException
	 */
	public List<Triplet> selectAll(boolean isSystem, int limit) throws DAOException;
	
	/**
	 * Select all the Triplet's instance with a possible limit and start
	 * 
	 * @param isSystem Select system's subject or not
	 * @param limit Max count of Triplet's instance. Put '-1' to select all without limit
	 * @param start Start to this number of Triplet's instance. Put '-1' to ignore this parameter
	 * @return List of Triplet's instance
	 * @throws DAOException
	 */
	public List<Triplet> selectAll(boolean isSystem, int limit, int start) throws DAOException;
	
	/**
	 * count the number of triplets
	 * 
	 * @return the number for triplets
	 * @throws DAOException
	 */
	public int countAll() throws DAOException;
	
	/**
	 * Select all the Triplet's instance with the chosen subject id and subject type
	 * 
	 * @param id Id of Triplet's subject (Subject.id or Triplet.id)
	 * @param type Subject's type : Subject or Triplet
	 * @param inverse If 'true', select the Triplet's instance with this id in complement
	 * @return List of Triplet's instance
	 * @throws DAOException
	 */
	public List<Triplet> selectAllBySubject(int id, TripletType type, boolean inverse) throws DAOException;
	
	/**
	 * Select all the Triplet's instance with the chosen relation id
	 * 
	 * @param id Id of Triplet's relation
	 * @return List of Triplet's instance
	 * @throws DAOException
	 */
	public List<Triplet> selectAllByRelation(int id) throws DAOException;
	
	/**
	 * Select all the Triplet's instance with the chosen subject id and relation id
	 * 
	 * @param idSubject Id of Triplet's subject
	 * @param idRelation Id of Triplet's relation
	 * @param type Subject's type : Subject or Triplet
	 * @return List of Triplet's instance
	 * @throws DAOException
	 */
	public List<Triplet> selectAllBySubjectAndRelation(int idSubject, int idRelation, TripletType type) throws DAOException;
	
	public List<Triplet> selectAllByRelationAndComplement(int idRelation, int idComplement, TripletType typeComplement, boolean inverse) throws DAOException;
	
	public List<Triplet> selectAll(int idSubject, int idRelation, int idComplement, TripletType typeSubject, TripletType typeComplement, boolean inverse, boolean isSystem) throws DAOException;
	
	/**
	 * Retrieve a list with the id of all the subject or complement having id as complement/subject respectively
	 * 
	 * @param id Id of a subject
	 * @throws DAOException
	 */
	public ArrayList<ArrayList<Integer>> selectForQuoiEntre(int id) throws DAOException;

	/**
	 * Delete the Triplet's instance by id
	 * 
	 * @param id Id of the Triplet's intance to delete
	 * @throws DAOException
	 */
	public void delete(int id) throws DAOException;
	
	/**
	 * Delete all Triplet's instance with the chosen subject id and type
	 * 
	 * @param id Id of Triplet's subject
	 * @param type Subject's type : Subject or Triplet
	 * @throws DAOException
	 */
	public void deleteBySubject(int id, TripletType type) throws DAOException;
	
	/**
	 * Delete all Triplet's instance with the chosen relation id
	 * 
	 * @param id Id of Triplet's relation
	 * @throws DAOException
	 */
	public void deleteByRelation(int id) throws DAOException;
	
	/**
	 * Delete all Triplet's instance with the chosen complement id and type
	 * 
	 * @param id Id of Triplet's complement
	 * @param type Complement's type : Subject or Triplet
	 * @throws DAOException
	 */
	public void deleteByComplement(int id, TripletType type) throws DAOException;
	
	/**
	 * Delete all Triplet's instance with the chosen subject, relation and complement
	 * 
	 * @param idSubject Id of Triplet's subject or '-1' to ignore it
	 * @param idRelation Id of Triplet's relation or '-1' to ignore it
	 * @param idComplement Id of Triplet's complement or '-1' to ignore it
	 * @param typeSubject Subject's type : Subject or Triplet
	 * @param typeComplement Complement's type : Subject or Triplet
	 * @throws DAOException
	 */
	public void deleteByAll(int idSubject, int idRelation, int idComplement, TripletType typeSubject, TripletType typeComplement) throws DAOException;
}