import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * The SpellChecker class provides the functionality for spell-checking
 * a document. By loading a document, and by initializing an English and
 * user dictionary, words from the document can be checked for misspelling,
 * miscapitalization, and double word errors. The SpellChecker class also
 * provides funcitonality for suggesting corrections to misspelling and 
 * miscapitalization errors. Additionally, the SpellChecker is integrated
 * with metrics to count the number of each error type.
 * 
 * @author      Joud Adel Al-Lahham <jallahha@uwo.ca>
 * @author      Jodi Keizer <jkeizer@uwo.ca>
 * @author      Anna Ma <ama92@uwo.ca>
 * @author      Ivan Quan <iquan5@uwo.ca>
 * @author      Kevin Xie <kxie49@uwo.ca>
 * @version     1.0
 */

public class SpellChecker {
  private EnglishDictionary englishDictionary;
  public UserDictionary userDictionary;
  public ArrayList<String> ignoredWords;
  private int currentWordIndex;
  private Document myDoc;
  public Metrics myMetrics;
  private ArrayList<String> errorType; 

  /**
   * Constructor for the SpellChecker class. Takes a file path as an 
   * argument to initializse the document, alongside an indication if
   * the doc is txt or html/xml. Initializes the document, as well as the
   * English and user dictionary. This function also counts the metrics
   * in the document for each error type, and automatically initializes
   * the SpellChecker's word index to the first error in the document.
   *
   * @param docPath the path of the document to be read
   * @param isHTML true if document is html/xml, false for txt file
   */
  public SpellChecker(String docPath, boolean isHTML) {
    myDoc = new Document(docPath, isHTML);
    englishDictionary = new EnglishDictionary();
    ignoredWords = new ArrayList<String>();
    String userDictFilePath = "user_dict.txt";
    userDictionary = new UserDictionary();
    userDictionary.loadDictionary(userDictFilePath);
    errorType = new ArrayList<String>();

    resetMetrics();
    currentWordIndex = 0;
    advanceNextError(); // instantiate index to first error
  }

  /**
   * Retrieves the current word index (current error)
   *
   * @return the integer containing the index of the current error in the document
   */
  public int getCurrentWordIndex() {
    return currentWordIndex;
  }

  /**
   * Retrieves the Document object being spell-checked
   *
   * @return the Document object being spell-checked
   */
  public Document getDocument() {
    return myDoc;
  }

  /**
   * Retrieves the current error type(s) at the current word index
   *
   * @return an ArrayList of the current error type(s) at the current word index
   */
  public ArrayList<String> getErrorType() {
    return errorType;
  }

  /**
   * Advances the current word index to the next error in the document
   */
  public void advanceNextError() {
    Boolean errorFound = false;
    while(!errorFound) {
      if(endOfDoc()) return; // if we are at the last word, return
      else currentWordIndex++;
      String currentWord = myDoc.getWordsList().get(currentWordIndex); // retrieve next word from document
      String currWordToCheck = stripPunctuation(currentWord);
      currWordToCheck = currWordToCheck.toLowerCase();

      if(currWordToCheck.equals("") || currWordToCheck == null) continue; // if word is empty/null, skip it
      if(currWordToCheck.equals("\n")) continue; // if word is just a newline, skip it
      if(isInteger(currWordToCheck)) continue; // if word is just numbers, skip it
      if(myDoc.isHTML && currentWord.startsWith("<") && currentWord.endsWith(">")) continue; //if html/xml, when finding next error, if word starts with < or ends with >, skip it 

      // update error type flags
      errorType.clear(); // reset error type
      if(detectMiscapitalizationError()) errorType.add("miscapitalized"); // if miscapitalization detected, update error type flags
      if(detectDoubleWordError()) errorType.add("double word"); // if double word detected, update error type flags
      if(detectMisspellingError()) errorType.add("misspelled"); //if misspelling detected, update error type flags

      if(detectMiscapitalizationError()) return; // if miscapitalization detected, we are at an error index. break.
      if(detectDoubleWordError()) return; // if double word detected, we are at an error index. break.
      if(detectMisspellingError()) return; //if misspelling detected, we are at an error index. break.
    }
  }

  /**
   * Detects if there is a miscapitalization at the current word index in the document
   * 
   * @return true if there is a miscapitalization at the current word index, false otherwise
   */
  private boolean detectMiscapitalizationError() { // detect miscapitalization error at the current word index
    String currentWord = myDoc.getWordsList().get(currentWordIndex);
    String currWordToCheck = stripPunctuation(currentWord);

    // detect miscapitalization error for capitalizing start of sentence
    String prevWord;
    boolean startOfSentence = false;
    if(currentWordIndex == 0) { // if index is on first word, return false
      startOfSentence = true;
    }
    else {
      prevWord = myDoc.getWordsList().get(currentWordIndex-1);
      if(prevWord.endsWith(".")) startOfSentence = true;
    }

    if(startOfSentence && !Character.isUpperCase(currWordToCheck.charAt(0))) {
      return true;
    }

    // detect miscapitalization error for mixed capitalization
    if(!currWordToCheck.equals(currWordToCheck.toUpperCase()) && !currWordToCheck.substring(1, currWordToCheck.length()).equals(currWordToCheck.substring(1, currWordToCheck.length()).toLowerCase())) {
      return true;
    }
      
    return false;
  }

  /**
   * Detects if there is a misspelling at the current word index in the document
   * 
   * @return true if there is a misspelling at the current word index, false otherwise
   */
  private boolean detectMisspellingError() { // detect misspelling error at the current word index
    String currentWord = myDoc.getWordsList().get(currentWordIndex);    
    String currWordToCheck = stripPunctuation(currentWord);
    currWordToCheck = currWordToCheck.toLowerCase();

    if(ignoredWords.contains(currWordToCheck)) return false;

    if(userDictionary.containsWord(currWordToCheck)) return false;

    if(englishDictionary.containsWord(currWordToCheck)) return false;

    return true; // if word isn't in ignored words or user/english dict, it is misspelled
  }

  /**
   * Detects if there is a double word error at the current word index in the document
   * 
   * @return true if there is a double word error at the current word index, false otherwise
   */
  private boolean detectDoubleWordError() { //detect double word error at the current word index
    String currentWord = myDoc.getWordsList().get(currentWordIndex);    
    String currWordToCheck = stripPunctuation(currentWord);
    currWordToCheck = currWordToCheck.toLowerCase();
    String prevWord;
    if(currentWordIndex == 0) { // if index is on first word, return false
      return false;
    }
    else {
      prevWord = myDoc.getWordsList().get(currentWordIndex-1);
      prevWord = stripPunctuation(prevWord);
      prevWord = prevWord.toLowerCase();
    }
    if(prevWord.equals(currWordToCheck)) return true; // double word detected, return true
    else return false; // not double word, return false
  }

  /**
   * Adds a word to the 'ignored words' list, as to not detect this word as an error
   * 
   * @param word word to be added to the ignored list
   */
  public void addToIgnoredWords(String word) {
    word = stripPunctuation(word);
    word = word.toLowerCase();
    ignoredWords.add(word);
  }

  /**
   * Suggests corrections for the error at the current word index. If there is a misspelling,
   * add suggestions for misspelling errors. If there is a miscapitalization, add suggestions
   * for miscapitalization errors.
   * 
   * @return an ArrayList<String> containing all potential corrections
   */
  public ArrayList<String> suggestCorrections() {

    ArrayList<String> suggestedCorrections = new ArrayList<String>();

    if(detectMiscapitalizationError()) suggestedCorrections.addAll(suggestMiscapitalizationCorrections());
    if(detectMisspellingError()) suggestedCorrections.addAll(suggestMisspellingCorrections());

    return suggestedCorrections;
  }

  /**
   * Suggests miscapitalization corrections for the error at the current word index.
   * Miscapitalizations include forgetting to capitalize the start of a sentence, or
   * having mixed capitalization in words.
   * 
   * @return an ArrayList<String> containing potential miscapitalization corrections
   */
  public ArrayList<String> suggestMiscapitalizationCorrections() {
    String errorWord = stripPunctuation(myDoc.getWordsList().get(currentWordIndex)); // get current error word
    ArrayList<String> suggestedCorrections = new ArrayList<String>();
    // detect miscapitalization error for capitalizing start of sentence
    String prevWord;
    boolean startOfSentence = false;
    if(currentWordIndex == 0) { // if index is on first word, return false
      startOfSentence = true;
    }
    else {
      prevWord = myDoc.getWordsList().get(currentWordIndex-1);
      if(prevWord.endsWith(".")) startOfSentence = true;
    }

    if(startOfSentence && !Character.isUpperCase(errorWord.charAt(0))) { // if start-of-sentence miscapitalization, add correctly capitalized word
      String lowerError = errorWord.toLowerCase();
      suggestedCorrections.add(lowerError.substring(0, 1).toUpperCase() + lowerError.substring(1));
    }

    // detect miscapitalization error for mixed capitalization
    if(!errorWord.equals(errorWord.toUpperCase()) && !errorWord.substring(1, errorWord.length()).equals(errorWord.substring(1, errorWord.length()).toLowerCase())) {
      suggestedCorrections.add(errorWord.toLowerCase()); // add lowercase word
      suggestedCorrections.add(errorWord.toUpperCase()); // add uppercase word

      String lowerError = errorWord.toLowerCase(); // add first letter capitalized if it's not already there
      if(!suggestedCorrections.contains(lowerError.substring(0, 1).toUpperCase() + lowerError.substring(1))) {
        suggestedCorrections.add(lowerError.substring(0, 1).toUpperCase() + lowerError.substring(1));
      }
    }
    return suggestedCorrections;
  }

  /**
   * Suggests misspelling corrections for the error at the current word index.
   * The current word is subjected to letter removal, letter insertion, letter
   * swapping, and word splitting to generate potential corrections, and these
   * potential corrections are checked against the English and user dictionary
   * to search for suggested corrections. Capitalization of the word is maintained.
   * 
   * @return an ArrayList<String> containing potential misspelling corrections
   */
  public ArrayList<String> suggestMisspellingCorrections() {
    String errorWord = myDoc.getWordsList().get(currentWordIndex); // get current error word
    errorWord = stripPunctuation(errorWord).toLowerCase(); // strip punctuation from current error word and converto to lower case
    ArrayList<String> potentialCorrections = new ArrayList<String>();

    potentialCorrections.addAll(removeLetters(errorWord)); // remove each letter from word and add to potentialCorrections
    potentialCorrections.addAll(insertLetters(errorWord)); // insert letters into word and add to potentialCorrections
    potentialCorrections.addAll(swapLetters(errorWord)); // swap consecutive letters in word and add to potentialCorrections

    ArrayList<String> potentialSplitWordSuggestions = new ArrayList<String>();
    potentialSplitWordSuggestions.addAll(splitWord(errorWord)); // add spaces in between word and add to potentialSplitWordSuggestions

    ArrayList<String> suggestedCorrections = new ArrayList<String>();
    for (String word : potentialCorrections) {
      if(englishDictionary.containsWord(word) && !suggestedCorrections.contains(word)) {
        suggestedCorrections.add(word);
      }
      else if(userDictionary.containsWord(word) && !suggestedCorrections.contains(word)) suggestedCorrections.add(word); // else if its in userdictionary, add
    }

    for (String splitWord : potentialSplitWordSuggestions) {
      String[] splits = splitWord.split(" "); // split the word
      if((englishDictionary.containsWord(splits[0]) || userDictionary.containsWord(splits[0])) &&
        (englishDictionary.containsWord(splits[1]) || userDictionary.containsWord(splits[1]))) {
        suggestedCorrections.add(splitWord);
        }
      }

    // retain capitalization state (all caps, first letter capitalization) for words corrected
    String currentWord = stripPunctuation(myDoc.getWordsList().get(currentWordIndex)); // get current word
    boolean isAllCaps = currentWord.equals(currentWord.toUpperCase()); // check if current word is all caps
    boolean firstLetterCapitalized = Character.isUpperCase(currentWord.charAt(0)); // check if current word's first letter is capitalized
    if(isAllCaps) {
      for(int i=0; i<suggestedCorrections.size(); i++) suggestedCorrections.set(i, suggestedCorrections.get(i).toUpperCase());
    }
    else if(firstLetterCapitalized) {
      for(int i=0; i<suggestedCorrections.size(); i++) {
        suggestedCorrections.set(i, suggestedCorrections.get(i).substring(0, 1).toUpperCase() + suggestedCorrections.get(i).substring(1));
      }
    }
    return suggestedCorrections;
  }

  /**
   * Removes each letter from the input word, and returns a list of words with 
   * each letter removed.
   * 
   * @param errorWord the word to remove letters from
   * @return an ArrayList<String> containing words with each letter removed
   */
  public static ArrayList<String> removeLetters(String errorWord) {
    ArrayList<String> potentialWords = new ArrayList<String>();
    String newWord;
    for(int i = 0; i < errorWord.length(); i++) { //iterate over letters in the input string
      newWord = errorWord.substring(0,i) + errorWord.substring(i+1); //omit one by one a character from the word
      potentialWords.add(newWord);
    }
    return potentialWords;
  }
  
  /**
   * Inserts each letter from the alphabet between positions of the input word,
   * and returns a list of words with a letter added.
   * 
   * @param errorWord the word to add letters to
   * @return an ArrayList<String> containing words with a letter added
   */
  public static ArrayList<String> insertLetters(String errorWord) {
    ArrayList<String> potentialWords = new ArrayList<String>();
    String newWord;
    for(int i = 0; i < errorWord.length() + 1; i++) { //iterate in between in the input string
      for(char c = 'a'; c <= 'z'; c++) { //iterate over characters in alphabet
        newWord = errorWord.substring(0,i) + c + errorWord.substring(i); //replace each letter in word with each letter in alphabet
        potentialWords.add(newWord);
      }
    }
    return potentialWords;
  }

  /**
   * Swaps the position of each adjacent letter from the input word, and
   * returns a list of these swapped-position words.
   * 
   * @param errorWord the word to swap letter positions
   * @return an ArrayList<String> containing words with swapped letter positions
   */
  public  static ArrayList<String> swapLetters(String errorWord) {
    ArrayList<String> potentialWords = new ArrayList<String>();
    String newWord;
    for(int i = 0; i < errorWord.length() - 1; i++) { //iterate over letters in the input string
      char tempChar1 = errorWord.charAt(i);
      char tempChar2 = errorWord.charAt(i + 1);
      newWord = errorWord.substring(0,i) + tempChar2 + tempChar1 + errorWord.substring(i+2); //reverse two characters from word
      potentialWords.add(newWord);
    }
    return potentialWords;
  }
  
  /**
   * Splits the input word with a space, between each letter and returns a 
   * list of these split words.
   * 
   * @param errorWord the word split
   * @return an ArrayList<String> containing the split words
   */
  public static ArrayList<String> splitWord(String errorWord) {
    ArrayList<String> potentialWords = new ArrayList<String>();
    String newWord;
    for(int i = 1; i < errorWord.length(); i++) { //iterate in between in the input string
      newWord = errorWord.substring(0,i) + " " + errorWord.substring(i); //replace each letter in word with each letter in alphabet
      potentialWords.add(newWord);
    }
    return potentialWords;
  }

  /**
   * Loads a new document, resets the metrics for the document, and advances
   * the current word index to the first error in the document
   * 
   * @param filePath the path of the document to be read
   * @param isHTML true if document is html/xml, false for txt file
   */
  public void loadNewDocument(String filePath, boolean isHTML) {
    myDoc = new Document(filePath, isHTML);
    resetMetrics();
    currentWordIndex = 0;
    advanceNextError();
  }

  /**
   * Resets all metrics to 0, and updates metrics to count the number of
   * misspellings, miscapitalizations, and double word errors in the 
   * document. Also resets the current word index to the index of the
   * first error in the document and sets the metrics count for document 
   * metrics.
   */
  private void resetMetrics() {
    currentWordIndex = 0;
    advanceNextError();
    myMetrics = new Metrics();
    while(!endOfDoc()){
      if(detectDoubleWordError()) myMetrics.increaseDoubleWord(1);
      if(detectMiscapitalizationError()) myMetrics.increaseMiscapitalization(1);
      if(detectMisspellingError()) myMetrics.increaseMisspelling(1);
      advanceNextError();
    }
    
    myMetrics.increaseWordCount(this.getDocument().docMetrics.getNumWords()); // set document metrics
    myMetrics.increaseCharacterCount(this.getDocument().docMetrics.getNumCharacters());
    myMetrics.increaseLineCount(this.getDocument().docMetrics.getNumLines());
    
    currentWordIndex = 0; // reset index back to first error
    advanceNextError();
  }

  /**
   * Checks if the current word index is the last word in the documentt.
   * 
   * @return true if the current word index is the last word in the document, false otherwise
   */
  public boolean endOfDoc() {
    return currentWordIndex >= (myDoc.getWordsList().size()-1);
  }

  /**
   * Checks if a given string is all numeric (can be converted to integer)
   * 
   * @param word the string to check if it is all numeric
   * @return true if the string can be converted to integer, false otherwise
   */
  public static boolean isInteger(String word) {
    try {
        Integer.parseInt(word);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}

  /**
   * Strips leading and trailing punctuation from a word.
   * 
   * @param word the word to strip puncutation from
   * @return the same word, without leading or trailing punctuation
   */
  public static String stripPunctuation(String word) {
    String resultWord = word;

    Pattern punctuationPattern = Pattern.compile("(.*?)([-.,;:!?(){}\"])*$");
    java.util.regex.Matcher matcher = punctuationPattern.matcher(resultWord);
    if (matcher.matches()) {
      if (matcher.group(1) != null) {
        resultWord = matcher.group(1); //extract word without leading/trailing punctuation
      } else {
        resultWord = ""; // word only contains punctuation, returns empty
      }
    }
    
    punctuationPattern = Pattern.compile("([\"-.,;:!?(){}])*(.*?)");
    matcher = punctuationPattern.matcher(resultWord);
    if (matcher.matches()) {
      if (matcher.group(2) != null) {
        resultWord = matcher.group(2); //extract word without leading/trailing punctuation
      } else {
        resultWord = ""; // word only contains punctuation, returns empty
      }
    }
    return resultWord;
  }
}
