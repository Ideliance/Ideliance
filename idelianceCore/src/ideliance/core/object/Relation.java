package ideliance.core.object;

import ideliance.core.config.ApplicationContext;
import ideliance.core.config.MyTimestamp;
import ideliance.core.object.type.DictionaryType;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class Relation implements Serializable {
	
	private int id;
	private Map<String, Dictionary> entitled;
	private Relation inverse;
	private Timestamp dateCreation;
	private Timestamp dateModification;
	private String authorCreation;
	private String authorModification;
	private boolean isSystem;
	
	public Relation() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		id = -1;
		entitled = new HashMap<String, Dictionary>();
		inverse = null;
		dateCreation = MyTimestamp.getCurrentTimestamp();
		dateModification = MyTimestamp.getCurrentTimestamp();
		authorCreation = context.getProperty("default.user");
		authorModification = context.getProperty("default.user");
		isSystem = false;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getEntitled(String lang) throws IllegalArgumentException {
		if (lang == null || lang.length() != 2) {
			throw new IllegalArgumentException("The parameter must be a string of length two");
		}
		
		if (entitled.get(lang) == null) {
			return "";
		}
		
		return entitled.get(lang).getValue();
	}
	
	public Map<String, Dictionary> getEntitledMap() {
		return entitled;
	}
	
	public void setEntitled(String lang, String value) throws IllegalArgumentException {
		if (lang == null || lang.length() != 2) {
			throw new IllegalArgumentException("The first parameter must be a string of length two");
		}
		
		Dictionary d = entitled.get(lang);
		
		if (d == null) {
			d = new Dictionary(lang, value, id, DictionaryType.RELATION_ENTITLED);
			
			entitled.put(lang, d);
		} else {
			d.setValue(value);
		}
	}
	
	public void setEntitledMap(Map<String, Dictionary> map) {
		entitled = map;
	}

	public Relation getInverse() {
		return inverse;
	}

	public void setInverse(Relation inverse) {
		this.inverse = inverse;
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

	public boolean isSystem() {
		return isSystem;
	}

	public void setIsSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}

	@Override
	public String toString() {
		String str = "----------------------------------\n"
				+ "Relation n°" + id + " :\n"
				+ "Entitled :\n";
		
		Set<String> keys = entitled.keySet();
		for (String key : keys) {
			str += "\t- " + key + " : " + entitled.get(key).getValue() + "\n";
		}
		
		str += "Technical datas :\n"
				+ "\t- Creation : " + dateCreation + " by " + authorCreation + "\n"
				+ "\t- Modification : " + dateModification + " by " + authorModification + "\n"
				+ "\t- Is system relation : " + isSystem + "\n"
				+ "----------------------------------";
		
		return str;
	}
}
