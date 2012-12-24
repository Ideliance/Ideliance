package ideliance.core.dao.exception;

@SuppressWarnings("serial")
public class DAOException extends Exception {

	public DAOException() {
		
	}
	
	public DAOException(String message) {
		super(message);
	}
}