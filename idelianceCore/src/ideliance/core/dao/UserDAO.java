package ideliance.core.dao;

import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.User;

import java.util.List;

public interface UserDAO {

	public int exists(String login, String password) throws DAOException;
	
	public List<User> selectAll() throws DAOException;
	
	public User selectSingle(int idUser) throws DAOException;
	
	public void add(User user) throws DAOException;
	
	public void update(User user) throws DAOException;
	
	public void delete(int idUser) throws DAOException;
}