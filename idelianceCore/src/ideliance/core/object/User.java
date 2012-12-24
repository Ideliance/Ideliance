package ideliance.core.object;

import ideliance.core.config.ApplicationContext;
import ideliance.core.config.MyTimestamp;
import ideliance.core.object.type.UserLevel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class User implements Serializable {

	private int id;
	private String login;
	private String password;
	private UserLevel level;
	private Map<String, String> config;
	private Timestamp dateCreation;
	private Timestamp dateModification;
	private String authorCreation;
	private String authorModification;
	
	public User() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		id = -1;
		login = null;
		password = null;
		level = null;
		config = new HashMap<String, String>();
		dateCreation = MyTimestamp.getCurrentTimestamp();
		dateModification = MyTimestamp.getCurrentTimestamp();
		authorCreation = context.getProperty("default.user");
		authorModification = context.getProperty("default.user");
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserLevel getLevel() {
		return level;
	}

	public void setLevel(UserLevel level) {
		this.level = level;
	}
	
	public String getConfig(String key) {
		return config.get(key);
	}
	
	public Map<String, String> getConfig() {
		return config;
	}
	
	public void setConfig(String key, String value) {
		config.put(key, value);
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
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
}