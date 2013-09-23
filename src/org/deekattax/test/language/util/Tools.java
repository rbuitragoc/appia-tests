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

  public Tools() {
    try {
      for (WordList enumItem: WordList.values()) {
        if (!enumItem.equals(WordList.end))
          loadDict(enumItem);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void loadDict(WordList dict) throws IOException {
    List<String> dictAsList = 
        FileUtils.readLines(new File("test/resources/" + dict.name() + ".dic"));
    words.put(dict.name(), dictAsList);
  }

  public String generateRandomWord(WordList list) {
    Random r = new SecureRandom();
    List<String> dict = words.get(list.name()); 
    return dict.get(r.nextInt(dict.size()));
  }

  public WordList getRandomWordList(WordList ... lists) {
    WordList selectedDict = lists[getRandomFromAvailableChoices(lists)];
    return selectedDict;
  }
  
  public int getRandomFromAvailableChoices(WordList ...lists) {
    if (lists == null) return 0;
    Random r = new SecureRandom();
    return r.nextInt(lists.length);
  }

  public String generateRandomName(WordList list) {
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