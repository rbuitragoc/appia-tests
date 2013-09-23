package org.deekattax.test.language;

import org.deekattax.test.language.util.Tools;
import org.deekattax.test.language.util.WordList;

public class Main {
  
  private static final String LB = System.getProperty("line.separator");
  
  private Tools tools;
  
  public Main() {
    tools = new Tools();
  }

  public static void main(String... args) {
    new Main().writeOutPoem(5);
  }
  
  private void writeOutPoem(int length) {
    for (int i = 0; i < length; i++) {
      System.out.print(writeLine());
    }
  }
  
  private String writeLine() {
    final int CHOICE = 
        tools.getRandomFromAvailableChoices(WordList.nouns, WordList.prepositions, WordList.pronouns);
    switch(CHOICE) {
      case 0: return writeNoun();
      case 1: return writePreposition();
      case 2: return writePronoun();
      default: return null;
    }
  }
  
  private String writeNoun() {
    return writeWordWithComplements(WordList.nouns, 0);
  }
  
  private String writePreposition() {
    return writeWordWithComplements(WordList.prepositions, 0);
  }
  
  private String writePronoun() {
    return writeWordWithComplements(WordList.pronouns, 0);
  }
  
  private String writeWordWithComplements(WordList wordList, int depth) {
//    System.err.println("Depth=" + depth);
    if (wordList.name().equals(WordList.end.name()) || depth >= Integer.MAX_VALUE)
      return LB;
    StringBuilder buf = new StringBuilder();
    String wordItself = tools.generateRandomWord(wordList);
    String complement = writeWordWithComplements(tools.getRandomWordList(wordList.complements()), ++depth);
    buf.append(wordItself).append(" ").append(complement);
    return buf.toString();
  }

}
