package ideliance.servlet.quoiEntre;

import java.io.Serializable;

import ideliance.core.config.ApplicationContext;
import ideliance.core.dao.DAOFactory;
import ideliance.core.dao.RelationDAO;
import ideliance.core.dao.SubjectDAO;
import ideliance.core.dao.exception.DAOException;
import ideliance.core.object.Relation;
import ideliance.core.object.Subject;

@SuppressWarnings("serial")
public class KeyGroup implements Serializable {
	private int idSubject = -1;
	private int idRelation = -1;
	private boolean isInverse = false;

	public KeyGroup(){
		 idSubject = -1;
		 idRelation =  -1;
		 isInverse = false;
	}
	public KeyGroup(int _idSubject, int _idRelation, boolean _isInverse){
		 idSubject =_idSubject;
		 idRelation = _idRelation;
		 isInverse = _isInverse;
	}
	private DAOFactory getDaoFactory(){
		ApplicationContext context = ApplicationContext.getInstance();
		DAOFactory daoFactory = null;
		 try {
			 daoFactory = DAOFactory.getDAOFactory(context.getIntProperty("dao.factory", -1));
			} catch (DAOException e) {
				//error
			}
		 return daoFactory;
	}
	public int getIdSubject() {
		return idSubject;
	}
	public Subject getSubject() {
		SubjectDAO subjectDao = getSubjectDao();
		try {
			return subjectDao.selectSingle(idSubject);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void setIdSubject(int idSubject) {
		this.idSubject = idSubject;
	}
	public int getIdRelation() {
		return idRelation;
	}
	public Relation getRelation() {
		RelationDAO relationDao = getRelationDao();
		try {
			/*
			 * TODO : 
			 * Selection par inverse
			 * diminution du nb de requête via le jsp.
			 * */
			System.out.println(isInverse);
			System.out.println(relationDao.selectSingle(idRelation, isInverse));
			return relationDao.selectSingle(idRelation, isInverse);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void setIdRelation(int idRelation) {
		this.idRelation = idRelation;
	}
	public boolean isInverse() {
		return isInverse;
	}
	public void setInverse(boolean isInverse) {
		this.isInverse = isInverse;
	}
	
	private SubjectDAO getSubjectDao(){
		SubjectDAO subjectDao = null;
		try {
			DAOFactory daoFactory = getDaoFactory();
			subjectDao = daoFactory.getSubjectDAO();
		} catch (DAOException e) {
			//error
		}
		return subjectDao;
	}
	private RelationDAO getRelationDao(){
		RelationDAO relationDao = null;
		try {
			DAOFactory daoFactory = getDaoFactory();
			relationDao = daoFactory.getRelationDAO();
		} catch (DAOException e) {
			//error
		}
		return relationDao;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idRelation;
		result = prime * result + idSubject;
		result = prime * result + (isInverse ? 1231 : 1237);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
//		System.out.println("EQUALS");
//		System.out.println(obj);
//		System.out.println(this);
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyGroup other = (KeyGroup) obj;
//		if (idRelation != other.idRelation)
//			return false;
		if (idSubject == other.idSubject)
			return true;
//		if (isInverse != other.isInverse)
//			return false;
		return false; 
	}
	@Override
	public String toString() {
		return "(s=" + idSubject + ", r="
				+ idRelation + ", I=" + isInverse + ")";
	}
	public String toJson() {
		String jsonRes = "";
		
		jsonRes += "{";
		
		jsonRes += "\"subject\":";
		jsonRes += "{\"id\":";
		jsonRes += idSubject;
		jsonRes += ",\"entitled\":";
		jsonRes += "\""+getSubject().getEntitled("en")+"\"";
		jsonRes += "}";
		
		jsonRes += ",\"relation\":";
		jsonRes += "{\"id\":";
		jsonRes += idRelation;
		jsonRes += ",\"entitled\":";
		jsonRes += "\""+getRelation().getEntitled("en")+"\"";
		jsonRes += ",\"isInverse\":";
		jsonRes += isInverse;
		jsonRes += "}";
		
		jsonRes += "}";
		
		return jsonRes;
	}
	
	public String toStringTable(){
		SubjectDAO subjectDao = getSubjectDao();
		RelationDAO relationDao = getRelationDao();
		
		Relation relation;
		Subject subject;
		try {
			relation = relationDao.selectSingle(idRelation, isInverse);
			subject = subjectDao.selectSingle(idSubject);
		} catch (DAOException e) {
			return "";
		}
		
		String content = "";
		if(idRelation!=-1){
			content += "<td>";
			content += relation.getEntitled("en");
			content += "</td>";
		}
		content += "<td>";
		content += subject.getEntitled("en");
		content += "</td>";
		return content;
	}
	
	
}
