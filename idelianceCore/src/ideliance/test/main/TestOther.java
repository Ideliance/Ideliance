package ideliance.test.main;

import ideliance.core.config.ApplicationContext;
import ideliance.core.config.MyTimestamp;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TestOther {

	public static void main(String[] args) {
		format(MyTimestamp.getCurrentTimestamp(), "HH:mm dd-MM-yyyy");
	}
	
	public static String format(Timestamp time, String format) {
		if (format == null) {
			format = ApplicationContext.getInstance().getProperty("default.date.format");
		}
		System.out.println("Format : " + format);
		SimpleDateFormat  simpleFormat = new SimpleDateFormat(format);
		String res = simpleFormat.format(time);
		
		System.out.println("Résultat : " + res);
		return res;
	}
}
