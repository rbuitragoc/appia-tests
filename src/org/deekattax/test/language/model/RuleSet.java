package org.deekattax.test.language.model;


import static org.deekattax.test.language.model.constant.ItemType.LB;
import static org.deekattax.test.language.model.constant.ItemType.fromRawItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deekattax.test.language.util.ErrorHandler;
import org.deekattax.test.language.util.Tools;

public class RuleSet {
	
	private Map<String, GrammarObject> rules = new HashMap<String, GrammarObject>();
	private List<String> referenced = new ArrayList<String>();
	private Tools tools;
	
	public RuleSet(File file, Tools tools) {
		this.tools = tools;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				this.addRule(line);
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			if (reader != null) {
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(">>> Rules before processing: " + rules.toString());
	}
	
	public String getTopLevelRule() {
		for (String rule: rules.keySet()) {
			if (!referenced.contains(rule)) {
				return rule;
			}
		}
		return null;
	}
	
	public GrammarObject getRule(String name) {
		return rules.get(name);
	}
	
	public void addRule(GrammarObject object) {
		rules.put(object.getName(), object);
	}
	
	public String addRule(String line) {
		if (line == null) {
			return ErrorHandler.handle("Null line!");
		}
		if (line.indexOf(":") == -1) {
			return ErrorHandler.handle("Can't find \':\', this is not a rule definition");
		}
		String[] nameAndContents = line.split("\\:");
		GrammarObject object = new GrammarObject();
		object.setName(nameAndContents[0]);
		object.setRawStructure(nameAndContents[1]);
		if (!rules.containsKey(object.getName())) {
			rules.put(object.getName(), object);
		}
		
		return object.getName();
	}

	public void processAllRules() {
		for (GrammarObject object: rules.values()) {
			if (isReferenced(object.getName())) {
				referenced.add(object.getName());
			}
			object.processStructure(this);
		}
		
	}
	
	private boolean isReferenced(String ruleName) {
		for (GrammarObject object: rules.values()) {
			if (object.references(ruleName)) {
				return true;
			}
		}
		return false;
	}
	
	public String sample() {
		return generate(getTopLevelRule(), new Tools());
	}
	
	public String sample(Tools tools) {
		return generate(getTopLevelRule(), tools);
	}
	
	public String generate(String ruleName, Tools tools) {
		GrammarObject rule = rules.get(ruleName);
		StringBuilder buf = new StringBuilder();
		switch (rule.getType()) {
			case RULE: {
				if (rule.hasRandomSubRules()) {
					String applyThisRule = tools.generateRandomWord(rule.getRandomSubRules());
					if (fromRawItem(applyThisRule).equals(LB)) {
						return appendLineBreak(applyThisRule, buf).toString();
					}
					buf.append(generate(applyThisRule, tools));
				} 
				
				if (rule.hasFixedRules()) {
					for (String subRule: rule.getFixedSubRules()) {
						if (fromRawItem(subRule).equals(LB)) {
							return appendLineBreak(subRule, buf).toString();
						}
						buf.append(generate(subRule, tools));
					}
				}
				break;
			}
			case CATEGORY: {
				String word = tools.generateRandomWord(rule.getWordlist());
				buf.append(word);
				
				if (rule.hasRandomSubRules()) {
					if (buf.length() > 0) {
						buf.append(" ");
					}
					String applyThisRule = tools.generateRandomWord(rule.getRandomSubRules());
					if (fromRawItem(applyThisRule).equals(LB)) {
						return appendLineBreak(applyThisRule, buf).toString();
					}
					buf.append(generate(applyThisRule, tools));
				}
				break;
			}
		}
		
	
		return buf.toString();
	}

	
	private StringBuilder appendLineBreak(String word, StringBuilder buf) {
		if (!buf.toString().contains(Tools.LB)) {
			buf.append(Tools.LB);
		}
		return buf;
	}
	
	@Override
	public String toString() {
		return "RuleSet [rules=" + rules + "]";
	}

}
