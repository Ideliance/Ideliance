package ideliance.core.config;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyTimestamp {

	public static Timestamp getCurrentTimestamp() {
		Calendar calendar = Calendar.getInstance();
		
		Date now = calendar.getTime();
		
		return new Timestamp(now.getTime());
	}
	
	public static String format(Timestamp time) {
		String format = ApplicationContext.getInstance().getProperty("default.date.format");
		
		return format(time, format);
	}
	
	public static String format(Timestamp time, String format) {
		if (format == null) {
			return format(time);
		}
		
		SimpleDateFormat  simpleFormat = new SimpleDateFormat(format);
		
		return simpleFormat.format(time);
	}
}