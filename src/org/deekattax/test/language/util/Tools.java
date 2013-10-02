package org.deekattax.test.language.util;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;

public class Tools {

  private Map<String, List<String>> words = new HashMap<String, List<String>>();
  public static final String LB = System.getProperty("line.separator");

  public Tools() {
    try {
      for (WordListEnumeration enumItem: WordListEnumeration.values()) {
        if (!enumItem.equals(WordListEnumeration.end))
          loadDict(enumItem);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void loadDict(WordListEnumeration dict) throws IOException {
    List<String> dictAsList = 
        FileUtils.readLines(new File("test/resources/" + dict.name() + ".dic"));
    words.put(dict.name(), dictAsList);
  }

  public String generateRandomWord(WordListEnumeration list) {
    Random r = new SecureRandom();
    List<String> dict = words.get(list.name()); 
    return dict.get(r.nextInt(dict.size()));
  }
  
  public String generateRandomWord(List<String> list) {
	  Random r = new SecureRandom();
	  return list.get(r.nextInt(list.size()));
  }

  public WordListEnumeration getRandomWordList(WordListEnumeration ... lists) {
    WordListEnumeration selectedDict = lists[getRandomFromAvailableChoices(lists.length)];
    return selectedDict;
  }
  
  public List<String> getRandomWordList(List<String>... lists) {
	  return lists[getRandomFromAvailableChoices(lists.length)];
  }
  
  public int getRandomFromAvailableChoices(int choicesCount) {
	Random r = new SecureRandom();
	return r.nextInt(choicesCount);
  }

  public String generateRandomName(WordListEnumeration list) {
    String randomName = "";
    String randomWord = generateRandomWord(list);
    if (randomWord.length() > 1) {
      String firstLetter = randomWord.substring(0, 1);
      randomName = firstLetter.toUpperCase() + randomWord.substring(1);
    } else {
      randomName = randomWord.toUpperCase();
    }
    return randomName;
  }
  
} 