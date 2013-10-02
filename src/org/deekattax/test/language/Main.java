package org.deekattax.test.language;

import java.io.File;

import org.deekattax.test.language.model.RuleSet;
import org.deekattax.test.language.util.Tools;
import org.deekattax.test.language.util.WordListEnumeration;

public class Main {

	private Tools tools;

	public Main() {
		tools = new Tools();
	}

	public static void main(String... args) {
		Main main = new Main();
		System.out
				.println(">>> Now writting out a poem by using the defined rules with the sourced word dictionaries: \n");
		long start = System.currentTimeMillis();
		main.readDictForPoemLines(5);
		long end = System.currentTimeMillis();
		StringBuilder report = new StringBuilder(String.format(
				"<<< First method took %d milliseconds", (end - start)));
		System.out
				.println("\n>>> Now writting out a poem by reading the rules from input file: \n");
		start = System.currentTimeMillis();
		main.parseRulesAndSample(new File("test/resources/rules.txt"));
		end = System.currentTimeMillis();
		report.append(String.format("; Second Method took %d milliseconds.",
				(end - start)));
		System.out.println(report.toString());
	}

	public void parseRulesAndSample(File file)  {
		RuleSet rules = new RuleSet(file, tools);
		rules.processAllRules();
		String topLevelRule = rules.getTopLevelRule();
		if (topLevelRule != null) {
			// System.out.println(String.format("I've found out that the top level rule is %s, so that's what I'm sampling ...\n",
			// topLevelRule));
			String writeOut = rules.generate(topLevelRule, tools);
			System.out.println(writeOut);
		}
	}

	private void readDictForPoemLines(int length) {
		for (int i = 0; i < length; i++) {
			System.out.print(writeLine());
		}
	}

	private String writeLine() {
		final int CHOICE = tools
				.getRandomFromAvailableChoices(new WordListEnumeration[] {
						WordListEnumeration.nouns,
						WordListEnumeration.prepositions,
						WordListEnumeration.pronouns }.length);
		switch (CHOICE) {
		case 0:
			return writeNoun();
		case 1:
			return writePreposition();
		case 2:
			return writePronoun();
		default:
			return null;
		}
	}

	private String writeNoun() {
		return writeWordWithComplements(WordListEnumeration.nouns, 0);
	}

	private String writePreposition() {
		return writeWordWithComplements(WordListEnumeration.prepositions, 0);
	}

	private String writePronoun() {
		return writeWordWithComplements(WordListEnumeration.pronouns, 0);
	}

	private String writeWordWithComplements(WordListEnumeration fixedList,
			int depth) {
		// System.err.println("Depth=" + depth);
		if (fixedList.name().equals(WordListEnumeration.end.name())
				|| depth >= Integer.MAX_VALUE)
			return Tools.LB;
		StringBuilder buf = new StringBuilder();
		String wordItself = tools.generateRandomWord(fixedList);
		String complement = writeWordWithComplements(
				tools.getRandomWordList(fixedList.complements()), ++depth);
		buf.append(wordItself).append(" ").append(complement);
		return buf.toString();
	}

}
