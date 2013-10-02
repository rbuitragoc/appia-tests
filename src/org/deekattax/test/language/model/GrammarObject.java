package org.deekattax.test.language.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deekattax.test.language.model.constant.GrammarType;
import org.deekattax.test.language.model.constant.ItemType;
import org.deekattax.test.language.util.ErrorHandler;
import org.deekattax.test.language.util.Tools;

/**
 * This class represents everything that can be extracted from a single line of a grammar-rule file text
 * @author rick
 */
public class GrammarObject {

	private String name;
	
	/**
	 * This will hold temporarily the substring that has all the sub rules and structure
	 */
	private String rawStructure;
	
	
	/**
	 * The Type Will be assigned as soon as it can be determined, its setting is delayed  
	 */
	private GrammarType type;
	
	/**
	 * Contains a raw structure split as the sequential, fixed items in a rule
	 */
	private List<String> structure = new ArrayList<String>();
	
	/**
	 * Contains a list of words among a category as defined by the corresponding rule
	 */
	private List<String> wordlist = new ArrayList<String>();
	
	
	/**
	 * Maps the optional and the mandatory rules. Initialized at the constructor.
	 */
	private Map<Boolean, List<String>> subRules;
	
	public GrammarObject() {
		this.subRules = new HashMap<Boolean, List<String>>();
		this.subRules.put(true, new ArrayList<String>());
		this.subRules.put(false, new ArrayList<String>());
	}
	
	public void processStructure(RuleSet rules) {
		String[] items = rawStructure.split("\\ ");
		for (String item : items) {
			switch (ItemType.fromRawItem(item)) {
				case PIPED: processPipedItem(item, rules); break;
				default: processSingleItem(item, rules, true); break;
			}
		}
		
		if (wordlist.isEmpty()) {
			setType(GrammarType.RULE);
		} else {
			setType(GrammarType.CATEGORY);
		}
	}


	private void processPipedItem(String item, RuleSet rules) {
		String[] pipedItems = item.split("\\|");
		for (String pipedItem : pipedItems) {
			processSingleItem(pipedItem, rules, false);
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	private void processSingleItem(String item, RuleSet rules, boolean isFixed) {
		switch (ItemType.fromRawItem(item)) {
			case RULE: {
				String name = item.substring(1, item.length() - 1);
				GrammarObject object = null;
				if ((object = rules.getRule(name)) == null) {
					ErrorHandler.handle(String.format("Cannot find rule definition for %s", name));
				} else {
					addToSubRules(isFixed, object.getName());
				}
				return;
			} 
			case LB: {
				addToSubRules(isFixed, item);
				return;
			}
			case WORD: {
				addToWordList(item);
				return;
			}
		}
	}
	
	private void addToSubRules(boolean isFixed, String item) {
		List<String> specificSubrules = subRules.get(isFixed);
		specificSubrules.add(item);
		subRules.put(isFixed, specificSubrules);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GrammarType getType() {
		return type;
	}

	public void setType(GrammarType type) {
		this.type = type;
	}	

	public String getRawStructure() {
		return rawStructure;
	}

	public void setRawStructure(String rawStructure) {
		this.rawStructure = rawStructure.trim();
	}

	public List<String> getWordlist() {
		return wordlist;
	}

	public void setWordlist(List<String> wordlist) {
		this.wordlist = wordlist;
	}

	public void addToWordList(String string) {
		this.wordlist.add(string);
	}

	@Override
	public String toString() {

		return "GrammarObject [name=" + name 
				+ (structure.isEmpty() ? ", "+ Tools.LB + "rawStructure=" + rawStructure : "")
				+ (type != null? ", " + Tools.LB + "type=" + type: "")
				+ (!structure.isEmpty() ? ", " + Tools.LB + "structure=" + structure : "")
				+ (!subRules.get(true).isEmpty() ? ", " + Tools.LB + "fixedSubrules=" + subRules.get(true): "")
				+ (!subRules.get(false).isEmpty() ? ", " + Tools.LB + "randomSubrules=" + subRules.get(false): "")
				+ (!wordlist.isEmpty() ? ", " + Tools.LB + "wordlist=" + wordlist: "") + "]"+ Tools.LB + Tools.LB;
	}


	public List<String> getStructure() {
		return structure;
	}


	public void setStructure(List<String> structurre) {
		this.structure = structurre;
	}


	public Map<Boolean, List<String>> getSubRules() {
		return subRules;
	}


	public void setSubRules(Map<Boolean, List<String>> subRules) {
		this.subRules = subRules;
	}
	
	public boolean references(String ruleName) {
		return rawStructure.contains(ruleName);
	}
	
	public List<String> getFixedSubRules() {
		return subRules.get(true);
	}
	
	public boolean hasFixedRules() {
		return !getFixedSubRules().isEmpty();
	}
	
	public List<String> getRandomSubRules() {
		return subRules.get(false);
	}
	
	public boolean hasRandomSubRules() {
		return !getRandomSubRules().isEmpty();
	}

}
