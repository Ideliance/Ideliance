package ideliance.test.main;

import ideliance.core.config.ApplicationContext;

public class TestApplicationContext {

	public static void main(String [] argv) {
		ApplicationContext context = ApplicationContext.getInstance();
		
		System.out.println("URL : " + context.getProperty("db.connection.url"));
	}
}
