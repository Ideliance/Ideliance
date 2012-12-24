package ideliance.core.object.type;

public enum DictionaryType {
	NONE(-1),
	SUBJECT_ENTITLED(1),
	SUBJECT_FREE_TEXT(2),
	RELATION_ENTITLED(3);
	
	private int value;
	
	private DictionaryType(int nb) {
		value = nb;
	}
	
	public int getValue() {
		return value;
	}
	
	public static DictionaryType fromInt(int nb) {
		for (DictionaryType d : DictionaryType.values()) {
			if (nb == d.value) {
				return d;
			}
		}
		
		return null;
	}
}