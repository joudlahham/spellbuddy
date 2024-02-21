import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Document class represents a document, providing functionalities
 * for reading a file into a words list, replacing words, getting 
 * the context before and after a word, and getting the file path.
 * It supports both plain text and HTML/XML files. It also supports saving
 * the words list by overwriting the file, or saving it into a new file.
 * 
 * @author      Joud Adel Al-Lahham <jallahha@uwo.ca>
 * @author      Jodi Keizer <jkeizer@uwo.ca>
 * @author      Anna Ma <ama92@uwo.ca>
 * @author      Ivan Quan <iquan5@uwo.ca>
 * @author      Kevin Xie <kxie49@uwo.ca>
 * @version     1.0
 */

public class Document {
  private ArrayList<String> wordsList = new ArrayList<String>();
  private String filePath;
  public Metrics docMetrics = new Metrics();
  public boolean isHTML = false;
  
  /**
   * Constructor for the document class. Takes a file path as an argument,
   * alongside indication if the doc is txt or html/xml. Constructs a
   * words list from the document to represent the words in the document.
   * Updates metrics to count words, characters, and lines in the document.
   * For html/xml files, tags surrounded by < > are treated as one word.
   *
   * @param path the path of the file to be read
   * @param isDocHTML true if file is html/xml, false for txt file
   */
  public Document(String path, boolean isDocHTML) { // constructor for document
    this.isHTML =  isDocHTML;
    filePath = path;
    if(!this.isHTML){ // if document NOT html/xml, don't split based on <>
      try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
          String[] words = line.split(" "); // split the line into words based on spaces
          for (String word : words) { // add each word to the wordsList
            if(word.contains("-") && !word.startsWith("-") && !word.endsWith("-")) { // if word is hyphenated, add it as 2 words
              String[] parts = word.split("-");
              String beforeHyphen = parts[0] + "-";
              String afterHyphen = parts[1];
              wordsList.add(beforeHyphen);
              wordsList.add(afterHyphen);
              docMetrics.increaseWordCount(2);
              docMetrics.increaseCharacterCount(word.length());
            }
            else {
              this.wordsList.add(word);
              docMetrics.increaseWordCount(1);
              docMetrics.increaseCharacterCount(word.length());
            }
          }
          // Add the newline as a separate word
          wordsList.add("\n");
          docMetrics.increaseLineCount(1);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    else if(this.isHTML){ // if document IS html/xml, split based on <>
      try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        Pattern pattern = Pattern.compile("<[^>]*>|\\S+"); // match words within angle brackets or non-whitespace characters
        while ((line = reader.readLine()) != null) {
          Matcher matcher = pattern.matcher(line); // split lines into words based on regex above
          while (matcher.find()) {
            String word = matcher.group();
            if(word.contains("<") && !word.startsWith("<")) { // if word has < in the middle, break it up before adding it to words list
              String[] wordParts = word.split("(?=<)|(?<=>)");
              if(wordParts[0].contains("-") && !wordParts[0].startsWith("-") && !wordParts[0].endsWith("-")) { // if word is hyphenated, add it as 2 words
                String[] hyphenatedParts = wordParts[0].split("-");
                String beforeHyphen = hyphenatedParts[0] + "-";
                String afterHyphen = hyphenatedParts[1];
                wordsList.add(beforeHyphen);
                wordsList.add(afterHyphen);
                docMetrics.increaseWordCount(2);
                docMetrics.increaseCharacterCount(wordParts[0].length());
              } else if(wordParts[0].contains(".") && !wordParts[0].startsWith(".") && !wordParts[0].endsWith(".")) {
                String[] hyphenatedParts = wordParts[0].split(".");
                String beforeHyphen = hyphenatedParts[0] + ".";
                String afterHyphen = hyphenatedParts[1];
                wordsList.add(beforeHyphen);
                wordsList.add(afterHyphen);
                docMetrics.increaseWordCount(2);
                docMetrics.increaseCharacterCount(wordParts[0].length());
              }
              else {
                wordsList.add(wordParts[0]);
                docMetrics.increaseWordCount(1);
                docMetrics.increaseCharacterCount(wordParts[0].length());
              }

              wordsList.add(wordParts[1]);
              docMetrics.increaseWordCount(1);
              docMetrics.increaseCharacterCount(wordParts[1].length());
            }
            else { // if word doesn't have < in the middle, add it without modification to the words list
              wordsList.add(word);
              docMetrics.increaseWordCount(1);
              docMetrics.increaseCharacterCount(word.length());
            }
          }
          // Add the newline as a separate word
          wordsList.add("\n");
          docMetrics.increaseLineCount(1);
        }
      } catch (Exception e) {
          e.printStackTrace();
      }
    }
  }

  /**
   * Retrieves the words list for a document
   *
   * @return an ArrayList containing strings representing each word in the file
   */
  public ArrayList<String> getWordsList() {
    return wordsList;
  }

  /**
   * Retrieves the file path for a document
   *
   * @return a String representing the file path of the selected file
   */
  public String getFilePath() {
    return filePath;
  }

  /**
   * Retrieves the context before a given word index from the selected file
   *
   * @param index the index to which the context before the word is retrieved
   * @return a String representing the context before a given word
   */
  public String getContextBefore(int index) {
    ArrayList<String> contextBefore = new ArrayList<String>();
    int numWords = 50;
    if (index >= 0) {
      int lineCount = 0;
      int start = Math.max(0, index - numWords); // calculate start index for context before
      for (int i = index-1; i >= start; i--) { // add words from the current index to the start index
        if(index<0) break; // break if index out of range
        contextBefore.add(0, wordsList.get(i)); // add word to beginning of array
        if(wordsList.get(i).equals("\n")) { // if too many newlines added, break (ensure not too much pre-context so error word can be displayed)
          lineCount++;
        }
        if(lineCount > 4) break;
      }
    }
    String output = String.join(" ", contextBefore);
    return output;
  }

  /**
   * Retrieves the context after a given word index from the selected file
   *
   * @param index the index to which the context after the word is retrieved
   * @return a String representing the context after a given word
   */
  public String getContextAfter(int index) {
    ArrayList<String> contextAfter = new ArrayList<String>();
    int numWords = 50;
    if (index >= 0 && index < wordsList.size()) {
      int end = Math.min(wordsList.size(), index + numWords); // calculate end index for context after
      for (int i = index + 1; i < end; i++) { // add words from the target index to the end index
          contextAfter.add(wordsList.get(i));
      }
    }
    String output = String.join(" ", contextAfter);
    return output;
  }

  /**
   * Replaces a word in the words list of the document
   *
   * @param index the index to the word will be replaced
   * @param newWord the word to replace the old word with
   */
  public void replaceWord(int index, String newWord) {
    String oldWord = this.wordsList.get(index);
    String savedNewWord = newWord;

    Pattern trailingPunctuation = Pattern.compile("(.*?)([-.,;:!?(){}\"]+)*$");
    Matcher matcher = trailingPunctuation.matcher(oldWord);
    matcher.find();
    String punctuationAfter = matcher.group(2); // group 2 contains the trailing punctuation
    if(punctuationAfter!=null) newWord = newWord + punctuationAfter;

    Pattern leadingPunctuation = Pattern.compile("([-.,;:!?(){}\"]+)*(.*?)");
    Matcher leadMatcher = leadingPunctuation.matcher(oldWord);
    leadMatcher.find();
    String punctuationBefore = leadMatcher.group(1); // group 1 contains the leading punctuation
    if(punctuationBefore!=null) newWord = punctuationBefore + newWord;

    if(savedNewWord == "" && punctuationAfter!=null && punctuationBefore!=null) newWord = ""; // if word has trailing and beginning punctuation
    else if(savedNewWord == "" && punctuationAfter==null && punctuationBefore==null) { // remove word if it contains nothing and has no punctuation
      wordsList.remove(index);
      return;
    }

    wordsList.set(index, newWord);
  }

  /**
   * Saves the words list into a new file
   *
   * @param newDoc the words list to be saved
   * @param path the path to save the new words list to
   */
  public void saveNewFile(ArrayList<String> newDoc, String path) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
      for (int i = 0; i<newDoc.size()-1; i++) {
        String word = newDoc.get(i);
        String nextWord = "";
        try {
          nextWord = newDoc.get(i+1);
        }
        catch(Exception e) {
          e.printStackTrace();
        }
        Pattern pattern = Pattern.compile("^[^a-zA-Z0-9]+$");
        java.util.regex.Matcher matcher = pattern.matcher(nextWord);
        Boolean nextHasNoAlphanumerics = matcher.matches();

        matcher = pattern.matcher(word);
        Boolean hasNoAlphanumerics = matcher.matches();
        
        if (word.contains("\n")) { // add newline without space after 
          writer.write(word);
        }
        else if(hasNoAlphanumerics){
          continue;
        }
        else if(word.equals("")){
          continue;
        }
        else if(nextHasNoAlphanumerics) {
          writer.write(word + nextWord + " ");
        } else { // add word with spaces after
            writer.write(word + " ");
        }
      }
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Saves the words list into the same file, overwriting it
   *
   * @param newDoc the words list to be saved
   */
  public void overwriteFile(ArrayList<String> newDoc) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      for (int i = 0; i<newDoc.size()-1; i++) {
        String word = newDoc.get(i);
        String nextWord = "";
        try {
          nextWord = newDoc.get(i+1);
        }
        catch(Exception e) {
          e.printStackTrace();
        }
        Pattern pattern = Pattern.compile("^[^a-zA-Z0-9]+$");
        java.util.regex.Matcher matcher = pattern.matcher(nextWord);
        Boolean nextHasNoAlphanumerics = matcher.matches();

        matcher = pattern.matcher(word);
        Boolean hasNoAlphanumerics = matcher.matches();
        
        if (word.contains("\n")) { // add newline without space after 
          writer.write(word);
        }
        else if(hasNoAlphanumerics){
          continue;
        }
        else if(word.equals("")){
          continue;
        }
        else if(nextHasNoAlphanumerics) {
          writer.write(word + nextWord + " ");
        } else { // add word with spaces after
            writer.write(word + " ");
        }
      }
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
