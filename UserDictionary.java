import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Collections;

/**
 * The UserDictionary class is responsible for loading a list of user defined words
 * into a user dictionary, and provides functionality for resetting, adding, modifying,
 * removing, checking, and saving the dictionary.
 * 
 * @author      Joud Adel Al-Lahham <jallahha@uwo.ca>
 * @author      Jodi Keizer <jkeizer@uwo.ca>
 * @author      Anna Ma <ama92@uwo.ca>
 * @author      Ivan Quan <iquan5@uwo.ca>
 * @author      Kevin Xie <kxie49@uwo.ca>
 * @version     1.0
 */

public class UserDictionary {
    private Hashtable<String, Boolean> userDictionary;

    /**
     * Constructor for the user dictionary class.
     */
    public UserDictionary() {
        userDictionary = new Hashtable<>();
    }

    /**
     * Resets the user dictionary to default state (empty).
     */
    public void resetDictionary() {
        userDictionary.clear();
    }

    /**
     * Saves the words in user dictionary to a file to be accessed in another
     * session of the program.
     * 
     * @param filePath the file path to save the user dictionary to
     */
    public void saveDictionary(String filePath) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(userDictionary);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the words in a file to the user dictionary
     * 
     * @param filePath the file path to load the user dictionary from
     */
    public void loadDictionary(String filePath) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            userDictionary = (Hashtable<String, Boolean>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a word (converted to lowecase) to the user dictionary
     * 
     * @param word the word to add to the user dictionary
     */
    public void addWord(String word) {
        userDictionary.put(word.toLowerCase(), true);
    }

    /**
     * Removes a word from the user dictionary
     * 
     * @param word the word to remove from the user dictionary
     */
    public void removeWord(String word) {
        userDictionary.remove(word.toLowerCase());
    }

    /**
     * Edits a word in the user dictionary
     * 
     * @param oldWord the word to be edited
     * @param newWord the word to replace the old word with
     */
    public void editWord(String oldWord, String newWord) {
        // Remove the old word and add the new one
        userDictionary.remove(oldWord.toLowerCase());
        addWord(newWord);
    }

    /**
     * Checks if a word (converted to lowercase) is in the user dictionary
     * 
     * @param word the word to check
     * @return true if word is in dictionary, false otherwise
     */
    public boolean containsWord(String word) {
        return userDictionary.containsKey(word.toLowerCase());
    }

    /**
     * Converts the keys (words) of the user dictionary into an array list
     *
     * @return an array list of the user dictionary words
     */
    public ArrayList<String> userDictToArrayList() {
        ArrayList<String> keysArrayList = new ArrayList<>();
        Enumeration<String> keysEnumeration = userDictionary.keys();
        while (keysEnumeration.hasMoreElements()) {
            keysArrayList.add(keysEnumeration.nextElement());
        }
        Collections.sort(keysArrayList);
        return keysArrayList;
    }
}

