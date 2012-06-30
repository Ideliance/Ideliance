package ideliance.taglib;

import java.util.List;

public class Taglib {

	public static boolean contains(List<Integer> tab, Integer nb) {
		if (tab != null) {
			for (int i : tab) {
				if (i == nb) {
					return true;
				}
			}
		}
		
		return false;
	}
}
