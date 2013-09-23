package org.deekattax.test.language.util;

public enum WordList {
  adjectives, nouns, pronouns, verbs, prepositions, end;
  
  public WordList[] complements() {
    switch(this) {
      case adjectives: return new WordList[] {nouns, adjectives, end}; 
      case nouns: return new WordList[] {verbs, prepositions, end}; 
      case pronouns: return new WordList[] {nouns, adjectives};
      case verbs: return new WordList[] {prepositions, pronouns, end}; 
      case prepositions: return new WordList[] {nouns, pronouns, adjectives};
      default: return null;
    }
  }
  
}
