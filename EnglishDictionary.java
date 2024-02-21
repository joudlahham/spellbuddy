import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * The EnglishDictionary class is responsible for loading a list of English words from a file 
 * into memory and providing a method to check if a particular word is in that list. 
 * 
 * @author      Joud Adel Al-Lahham <jallahha@uwo.ca>
 * @author      Jodi Keizer <jkeizer@uwo.ca>
 * @author      Anna Ma <ama92@uwo.ca>
 * @author      Ivan Quan <iquan5@uwo.ca>
 * @author      Kevin Xie <kxie49@uwo.ca>
 * @version     1.0
 */

public class EnglishDictionary {
    HashSet<String> englishDictionary;

    /**
     * Constructor for the english dictionary class. Also loads the dictionary.
     */
    public EnglishDictionary() {
        englishDictionary = new HashSet<>();
        loadDictionary();
    }

    /**
     * Scans the dictionary file to add words to the english dictionary
     */
    private void loadDictionary() {
        // Load the dictionary from a file into the HashSet for quick lookup
        try (Scanner scanner = new Scanner(new File("words_alpha.txt"))) {
            while (scanner.hasNext()) {
                englishDictionary.add(scanner.next().toLowerCase());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Dictionary file could not be found.");
        }
    }

    /**
     * Checks if a word, when converted to lowercase, is in the english dictionary or not.
     * 
     * @return a boolean containing true if the word is in the english dictionary, false otherwise
     */
    public boolean containsWord(String word) {
        // Check if the word is in the dictionary
        return englishDictionary.contains(word.toLowerCase());
    }
}
