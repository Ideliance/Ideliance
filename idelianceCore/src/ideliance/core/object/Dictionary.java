package ideliance.core.object;

import java.io.Serializable;

import ideliance.core.config.ApplicationContext;
import ideliance.core.object.type.DictionaryType;

@SuppressWarnings("serial")
public class Dictionary implements Serializable {

	private int fk;
	private DictionaryType type;
	private String lang;
	private String value;
	
	public Dictionary() {
		ApplicationContext context = ApplicationContext.getInstance();
		
		fk = -1;
		type = DictionaryType.fromInt(context.getIntProperty("default.dictionary.type", -1));
		setLang(context.getProperty("default.dictionary.lang"));
		value = "";
	}
	
	public Dictionary(String lang, String value, DictionaryType type) {
		fk = -1;
		this.type = type;
		setLang(lang);
		this.value = value;
	}
	
	public Dictionary(String lang, String value, int fk, DictionaryType type) {
		this.fk = fk;
		this.type = type;
		setLang(lang);
		this.value = value;
	}
	
	public int getFk() {
		return fk;
	}
	
	public void setFk(int fk) {
		this.fk = fk;
	}
	
	public DictionaryType getType() {
		return type;
	}
	
	public void setType(DictionaryType type) {
		this.type = type;
	}
	
	public String getLang() {
		return lang;
	}
	
	public void setLang(String lang) throws IllegalArgumentException {
		if (lang == null || lang.length() != 2) {
			throw new IllegalArgumentException("The parameter must be a string of length two");
		}
		
		this.lang = lang;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return lang + " -> " + value;
	}
}
