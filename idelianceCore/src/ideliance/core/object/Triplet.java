package ideliance.core.object;

import ideliance.core.config.ApplicationContext;
import ideliance.core.config.MyTimestamp;
import ideliance.core.object.type.TripletType;

import java.io.Serializable;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class Triplet implements Serializable {

	private int id;
	private Subject sSubject;
	private Triplet tSubject;
	private Relation relation;
	private Subject sComplement;
	private Triplet tComplement;
	private Timestamp dateCreation;
	private Timestamp dateModification;
	private String authorCreation;
	private String authorModification;
	private TripletType typeSubject;
	private TripletType typeComplement;
	private boolean isSystem;
	
	public Triplet() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		id = -1;
		sSubject = null;
		tSubject = null;
		relation = null;
		sComplement = null;
		tComplement = null;
		dateCreation = MyTimestamp.getCurrentTimestamp();
		dateModification = MyTimestamp.getCurrentTimestamp();
		authorCreation = context.getProperty("default.user");
		authorModification = context.getProperty("default.user");
		typeSubject = TripletType.NONE;
		typeComplement = TripletType.NONE;
		isSystem = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Subject getSSubject() {
		return sSubject;
	}

	public void setSSubject(Subject subject) {
		sSubject = subject;
		tSubject = null;
		typeSubject = TripletType.SUBJET;
	}

	public Triplet getTSubject() {
		return tSubject;
	}

	public void setTSubject(Triplet triplet) {
		sSubject = null;
		tSubject = triplet;
		typeSubject = TripletType.TRIPLET;
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public Subject getSComplement() {
		return sComplement;
	}

	public void setSComplement(Subject subject) {
		sComplement = subject;
		tComplement = null;
		typeComplement = TripletType.SUBJET;
	}

	public Triplet getTComplement() {
		return tComplement;
	}

	public void setTComplement(Triplet triplet) {
		sComplement = null;
		tComplement = triplet;
		typeComplement = TripletType.TRIPLET;
	}

	public Timestamp getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(long time) {
		dateCreation.setTime(time);
	}

	public Timestamp getDateModification() {
		return dateModification;
	}

	public void setDateModification(long time) {
		dateModification.setTime(time);
	}

	public String getAuthorCreation() {
		return authorCreation;
	}

	public void setAuthorCreation(String authorCreation) {
		this.authorCreation = authorCreation;
	}

	public String getAuthorModification() {
		return authorModification;
	}

	public void setAuthorModification(String authorModification) {
		this.authorModification = authorModification;
	}

	public TripletType getTypeSubject() {
		return typeSubject;
	}

	public TripletType getTypeComplement() {
		return typeComplement;
	}

	public boolean isSystem() {
		return isSystem;
	}

	public void setIsSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	
	public String toString(String lang) throws IllegalArgumentException {
		if (lang == null || lang.length() != 2) {
			throw new IllegalArgumentException("The parameter must be a string of length two");
		}
		
		String str;
		
		str = (typeSubject == TripletType.SUBJET) ? sSubject.getEntitled(lang) : "(" + tSubject.toString(lang) + ")";
		str += " " + relation.getEntitled(lang) + " ";
		str += (typeComplement == TripletType.SUBJET) ? sComplement.getEntitled(lang) : "(" + tComplement.toString(lang) + ")";
		
		return str;
	}
}
