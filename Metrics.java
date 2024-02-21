/**
 * The Metrics class represents a set of metrics for each Document the user runs
 * SpellChecker on. It has functions for getting and incrementing the tracked
 * metrics, which include the number of lines, characters, and words in the
 * document; number of each error type in the document (misspelling,
 * miscapitalization, and double words errors); and number of each type of error
 * correction used (accepted suggestions, manual corrections, and deletions).
 * 
 * @author Joud Adel Al-Lahham <jallahha@uwo.ca>
 * @author Jodi Keizer <jkeizer@uwo.ca>
 * @author Anna Ma <ama92@uwo.ca>
 * @author Ivan Quan <iquan5@uwo.ca>
 * @author Kevin Xie <kxie49@uwo.ca>
 * @version 1.0
 */

public class Metrics {
  // Have to initiate public variables that UnitTestMetrics class can access
  public int numCharacters;
  public int numLines;
  public int numWords;
  public int numMisspellingError;
  public int numMiscapitalizationError;
  public int numDoubleWordsError;
  public int numAcceptedSuggestions;
  public int numDeletions;
  public int numManualCorrections;

  /**
   * Constructor for the Metrics class. Uses integer data types to store the
   * number of characters, words, and lines; the number of each error type, and
   * the number of each correction type.
   */
  public Metrics() {
    this.numCharacters = 0;
    this.numLines = 0;
    this.numWords = 0;
    this.numMisspellingError = 0;
    this.numMiscapitalizationError = 0;
    this.numDoubleWordsError = 0;
    this.numAcceptedSuggestions = 0;
    this.numDeletions = 0;
    this.numManualCorrections = 0;
  }

  // Getter methods for each variable
  /**
   * Retrieves the number of characters (excluding spaces) in a document.
   * 
   * @return the number of characters in the document.
   */
  public int getNumCharacters() {
    return numCharacters;
  }

  /**
   * Retrieves the number of lines in a document.
   *
   * @return the number of lines in the document.
   */
  public int getNumLines() {
    return numLines;
  }

  /**
   * Retrieves the number of words in a document.
   * Hyphenated words are counted as two words.
   * 
   * @return the number of words in the document.
   */
  public int getNumWords() {
    return numWords;
  }

  /**
   * Retrieves the number of misspelling errors in a document.
   *
   * @return the number of misspelling errors in a document.
   */
  public int getNumMisspellingError() {
    return numMisspellingError;
  }

  /**
   * Retrieves the number of miscapitalization errors in a document.
   *
   * @return the number of miscapitalization errors in a document.
   */
  public int getNumMiscapitalizationError() {
    return numMiscapitalizationError;
  }

  /**
   * Retrieves the number of double words errors in a document.
   *
   * @return the number of double words errors in a document.
   */
  public int getNumDoubleWordsError() {
    return numDoubleWordsError;
  }

  /**
   * Retrieves the number of errors fixed by accepting a suggestion in the
   * document.
   *
   * @return the number of accepted suggestions when fixing errors in a document.
   */
  public int getNumAcceptedSuggestions() {
    return numAcceptedSuggestions;
  }

  /**
   * Retrieves the number of errors fixed by deleting a word in the document.
   *
   * @return the number of deleted words when fixing errors in a document.
   */
  public int getNumDeletions() {
    return numDeletions;
  }

  /**
   * Retrieves the number of errors fixed by manual corrections in the document.
   *
   * @return the number of manual corrections for errors in a document.
   */
  public int getNumManualCorrections() {
    return numManualCorrections;
  }

  // Increases each metric
  /**
   * Increments the metric for the number of characters by a specified number.
   *
   * @param count number of characters to be added to the total character count.
   */
  public void increaseCharacterCount(int count) {
    this.numCharacters += count;
  }

  /**
   * Increments the metric for the number of lines by a specified number.
   *
   * @param count the number of lines to be added to the total line count.
   */
  public void increaseLineCount(int count) {
    this.numLines += count;
  }

  /**
   * Increments the metric for the number of words by a specified number.
   *
   * @param count the number of words to be added to the total word count.
   */
  public void increaseWordCount(int count) {
    this.numWords += count;
  }

  /**
   * Increments the metric for the number of misspelling errors by a specified
   * number.
   *
   * @param count the number of misspelling errors to be added to the total count.
   */
  public void increaseMisspelling(int count) {
    this.numMisspellingError += count;
  }

  /**
   * Increments the metric for the number of miscapitalization errors by a
   * specified number.
   *
   * @param count the number of misspelling errors to be added to the total count.
   */
  public void increaseMiscapitalization(int count) {
    this.numMiscapitalizationError += count;
  }

  /**
   * Increments the metric for the number of double word errors by a
   * specified number.
   *
   * @param count the number of double word errors to be added to the total count.
   */
  public void increaseDoubleWord(int count) {
    this.numDoubleWordsError += count;
  }

  /**
   * Increments the metric for the number of accepted suggestions.
   *
   * @param count the number of accepted suggestions to be added to the total
   *              count.
   */
  public void increaseAcceptedSuggestions(int count) {
    this.numAcceptedSuggestions += count;
  }

  /**
   * Increments the metric for the number of words deleted to fix errors.
   *
   * @param count the number of deleted words to be added to the total count.
   */
  public void increaseDeletions(int count) {
    this.numDeletions += count;
  }

  /**
   * Increments the metric for the number of manual error corrections.
   *
   * @param count the number of manual corrections to be added to the total count.
   */
  public void increaseManualCorrections(int count) {
    this.numManualCorrections += count;
  }

}
