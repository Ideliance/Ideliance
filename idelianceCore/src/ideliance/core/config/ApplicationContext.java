package ideliance.core.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class ApplicationContext extends Properties {

	private static final Logger log = Logger.getLogger(ApplicationContext.class);
	
//	private static final String PROPERTIES_FILE = "C:/Program Files/Apache Software Foundation/Tomcat 7.0/webapps/IdelianceTomcat/WEB-INF/src/ideliance.properties";
	private static final String PROPERTIES_FILE = "./ideliance.properties";
	
	private static ApplicationContext _instance = null;
	
	private ApplicationContext() {
		
		try {
			FileReader fileReader = new FileReader(PROPERTIES_FILE);
		
			try {
				load(fileReader);
			} finally {
				fileReader.close();
			}
		} catch (FileNotFoundException e) {
			log.error("This file does not exist : " + PROPERTIES_FILE, e);
		} catch (IOException e) {
			log.error("An error occurs during the context initialization", e);
		} 
	}
	
	public static ApplicationContext getInstance() {
		if (_instance == null) {
			_instance = new ApplicationContext();
		}
		
		return _instance;
	}
	
	public int getIntProperty(String key, int defaultValue) {
		int nb;
		String property = getProperty(key);
		
		try {
			nb = Integer.parseInt(property);
		} catch (NumberFormatException e) {
			nb = defaultValue;
		}
		
		return nb;
	}
}
