import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class UnitTestSpellChecker {
  // Test 1: testing if adding word to ignored list works, and converts to lowercase and strips punctuation before adding
  @Test
  void testAddToIgnored() {
    SpellChecker mySpellChecker = new SpellChecker("testDocument.txt", false);
    mySpellChecker.addToIgnoredWords("thisword");
    assertTrue(mySpellChecker.ignoredWords.contains("thisword"));
    mySpellChecker.addToIgnoredWords("FULLCAPITALIZATION");
    assertTrue(mySpellChecker.ignoredWords.contains("fullcapitalization"));
    mySpellChecker.addToIgnoredWords("Firstcapitalization");
    assertTrue(mySpellChecker.ignoredWords.contains("firstcapitalization"));
    mySpellChecker.addToIgnoredWords("MixedCapiTALIZaTION");
    assertTrue(mySpellChecker.ignoredWords.contains("mixedcapitalization"));
    mySpellChecker.addToIgnoredWords("())())...!stripPunctuation..!!!!.?");
    assertTrue(mySpellChecker.ignoredWords.contains("strippunctuation"));
    mySpellChecker.addToIgnoredWords(".!?stripleading");
    assertTrue(mySpellChecker.ignoredWords.contains("stripleading"));
    mySpellChecker.addToIgnoredWords("striptrailing??!");
    assertTrue(mySpellChecker.ignoredWords.contains("striptrailing"));
    assertFalse(mySpellChecker.ignoredWords.contains("shouldntbehere"));
  }

  // Test 2: testing the removeLetters function, which should remove each letter from a word and add them to an ArrayList<String>
  @Test
  void testRemoveLetters() {
    String myWord = "abcde";
    ArrayList<String> removed = SpellChecker.removeLetters(myWord);
    assertTrue(removed.contains("bcde"));
    assertTrue(removed.contains("acde"));
    assertTrue(removed.contains("abde"));
    assertTrue(removed.contains("abce"));
    assertTrue(removed.contains("abcd"));
  }

  // Test 3: testing the insertLetters function, which should insert each letter into each position of a word and add them to an ArrayList<String>
  @Test
  void testInsertLetters() {
    String myWord = "abcde";
    ArrayList<String> inserted = SpellChecker.insertLetters(myWord);
    assertTrue(inserted.contains("aabcde"));
    assertTrue(inserted.contains("babcde"));
    assertTrue(inserted.contains("oabcde"));
    assertTrue(inserted.contains("yabcde"));
    assertTrue(inserted.contains("zabcde"));
    assertTrue(inserted.contains("ajbcde"));
    assertTrue(inserted.contains("abicde"));
    assertTrue(inserted.contains("abcxde"));
    assertTrue(inserted.contains("abcdqe"));
    assertTrue(inserted.contains("abcdeb"));
    assertTrue(inserted.contains("abcdey"));
    assertTrue(inserted.contains("abcdez"));
  }

  // Test 4: testing the swapLetters function, which should swap the position of adjacent letters of a word and add them to an ArrayList<String>
  @Test
  void testSwapLetters() {
    String myWord = "abcde";
    ArrayList<String> swapped = SpellChecker.swapLetters(myWord);
    assertTrue(swapped.contains("bacde"));
    assertTrue(swapped.contains("acbde"));
    assertTrue(swapped.contains("abdce"));
    assertTrue(swapped.contains("abced"));
    
    myWord = "a";
    swapped = SpellChecker.swapLetters(myWord);
    assertEquals(0, swapped.size());
  }

  // Test 5: testing the splitWord function, which should split a word at each position with a space and add them to an ArrayList<String>
  @Test
  void testSplitWord() {
    String myWord = "abcde";
    ArrayList<String> split = SpellChecker.splitWord(myWord);
    assertTrue(split.contains("a bcde"));
    assertTrue(split.contains("ab cde"));
    assertTrue(split.contains("abc de"));
    assertTrue(split.contains("abcd e"));
    
    myWord = "a";
    split = SpellChecker.swapLetters(myWord);
    assertEquals(0, split.size());
  }

  // Test 6: testing the isInteger function, which should return true if a string can be converted to an integer
  @Test
  void testIsInteger() {
    assertTrue(SpellChecker.isInteger("8329"));
    assertTrue(SpellChecker.isInteger("0"));
    assertTrue(SpellChecker.isInteger("12"));
    assertTrue(SpellChecker.isInteger("-321893271"));
    assertFalse(SpellChecker.isInteger("apple"));
    assertFalse(SpellChecker.isInteger("j22h"));
    assertFalse(SpellChecker.isInteger("123k"));
    assertFalse(SpellChecker.isInteger("o23"));
    assertFalse(SpellChecker.isInteger("21."));
    assertFalse(SpellChecker.isInteger("!2313"));
  }

  // Test 7: testing the stripPunctuation function, which should return the same string, without leading and trailing punctuation
  @Test
  void testStripPunctuation() {
    assertEquals("apple", SpellChecker.stripPunctuation("apple"));
    assertEquals("apple", SpellChecker.stripPunctuation("!apple"));
    assertEquals("apple", SpellChecker.stripPunctuation("apple?"));
    assertEquals("apple", SpellChecker.stripPunctuation(".apple!"));
    assertEquals("apple", SpellChecker.stripPunctuation("!!(apple"));
    assertEquals("ap?ple", SpellChecker.stripPunctuation(".ap?ple!"));
    assertEquals("apple", SpellChecker.stripPunctuation("apple\"\")."));
    assertEquals("apple", SpellChecker.stripPunctuation("!??!?!?....,,.,.apple,.,.?!?!"));
    assertEquals("", SpellChecker.stripPunctuation("!!??..,,()()()))((,.,.!))"));
  }
}
