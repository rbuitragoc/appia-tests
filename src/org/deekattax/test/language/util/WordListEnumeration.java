package org.deekattax.test.language.util;

public enum WordListEnumeration {
  adjectives, nouns, pronouns, verbs, prepositions, end;
  
  public WordListEnumeration[] complements() {
    switch(this) {
      case adjectives: return new WordListEnumeration[] {nouns, adjectives, end}; 
      case nouns: return new WordListEnumeration[] {verbs, prepositions, end}; 
      case pronouns: return new WordListEnumeration[] {nouns, adjectives};
      case verbs: return new WordListEnumeration[] {prepositions, pronouns, end}; 
      case prepositions: return new WordListEnumeration[] {nouns, pronouns, adjectives};
      default: return null;
    }
  }
  
}
