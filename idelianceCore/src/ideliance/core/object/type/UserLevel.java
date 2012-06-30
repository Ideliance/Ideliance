package ideliance.core.object.type;

public enum UserLevel {
	USER("USER"),
	ADMINISTRATOR("ADMINISTRATOR");
	
	private String value;
	
	private UserLevel(String text) {
		value = text;
	}
	
	public String getValue() {
		return value;
	}
	
	public static UserLevel fromString(String text) {
		if (text != null) {
			for (UserLevel u : UserLevel.values()) {
				if (text.equals(u.value)) {
					return u;
				}
			}
		}
		
		return null;
	}
}