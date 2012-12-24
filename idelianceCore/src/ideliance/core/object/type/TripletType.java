package ideliance.core.object.type;

public enum TripletType {
	NONE(-1),
	SUBJET(0),
	TRIPLET(1);
	
	private int value;
	
	private TripletType(int nb) {
		value = nb;
	}
	
	public int getValue() {
		return value;
	}
	
	public static TripletType fromInt(int nb) {
		for (TripletType v : TripletType.values()) {
			if (nb == v.value) {
				return v;
			}
		}
		
		return null;
	}
}