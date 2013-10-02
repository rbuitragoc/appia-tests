package org.deekattax.test.language.model.constant;


public enum ItemType {
	RULE, WORD, LB, PIPED;
	
	public static ItemType fromRawItem(String item) {
		if (item.contains("|")) {
			return PIPED;
		} else if (item.startsWith("$")) {
			return LB;
		} else if (item.startsWith("<") && item.endsWith(">")) {
			return RULE;
		} else {
			return WORD;
		}
	}
}
