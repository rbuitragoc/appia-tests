package org.deekattax.test.language.util;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ToolsTest {

	private Tools underTest;

	@Before
	public void pre() {
		underTest = new Tools();
	}

	@Test
	public void testGenerateRandomWord() {
		String randomWord = underTest.generateRandomWord(WordList.adjectives);
		System.out.println("JUnit testing testGenerateRandomWord - " + randomWord);
		assertNotNull(randomWord);
		assertTrue(randomWord.length() > 0);
	}

	@Test
	public void testGenerateRandomName() {
		String randomName = underTest.generateRandomName(WordList.prepositions);
		System.out.println("JUnit testing testGenerateRandomName - " + randomName);
		assertNotNull(randomName);
		assertTrue(randomName.length() > 0);
		assertTrue(Character.isUpperCase(randomName.charAt(0)));
	}
	
	@Test
	public void testGetRandomFromAvailableChoices() {
	  Set<WordList> matched  = Collections.synchronizedSet(new HashSet<WordList>());
	  
	  // we give enough chances for the random seed to pick up all options, say 30 rounds
	  for (int i = 0; i < 30; i++) {
	    WordList dict = underTest.getRandomWordList(WordList.values());
	    assertNotNull(dict);
	    
	    if (!matched.contains(dict)) {
	      matched.add(dict);
	    } 
	  }
	  
	  // and in the end, we should have all options picked up
	  assertEquals(WordList.values().length, matched.size());
	  
	}

}
