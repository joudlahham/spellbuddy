import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTestSpellchecker {
    // Test 1: Tests that the SpellChecker class correctly interacts with the EnglishDictionary class when the checked word is not present in the English dictionary
    @Test
    void invalidWordTest() {
        SpellChecker spellChecker = new SpellChecker("testDocument.txt", false);
        EnglishDictionary englishDictionary = new EnglishDictionary();
        spellChecker.advanceNextError();
        boolean isInDictionary = englishDictionary.containsWord("gourpsevntn"); // Word that is not in the dictionary
        assertFalse(isInDictionary, "gourpsevntn should not be in the English dictionary.");
    }

    // Test 2: Test that the SpellChecker class correctly interacts with the UserDictionary class when adding a word to the user dictionary
    @Test
    void addToUserDictionaryTest() {
        SpellChecker spellChecker = new SpellChecker("testDocument.txt", false);
        UserDictionary userDictionary = new UserDictionary();
        spellChecker.suggestCorrections();
        String newWord = "addedtouser"; // String to be added to user dictionary
        userDictionary.addWord(newWord);
        boolean isInUserDictionary = userDictionary.containsWord(newWord);
        assertTrue(isInUserDictionary, "addedtouser should be in the user dictionary.");
    }

    // Test 3: Test that the SpellChecker class correctly interacts with the UserDictionary class when removing a word from the user dictionary
    @Test
    void removeFromUserDictionaryTest() {
        SpellChecker spellChecker = new SpellChecker("testDocument.txt", false);
        UserDictionary userDictionary = new UserDictionary();
        String wordToRemove = "addedtouser"; // Previously added to user dictionary
        userDictionary.addWord(wordToRemove);
        spellChecker.suggestCorrections();
        userDictionary.removeWord(wordToRemove);
        boolean isInUserDictionary = userDictionary.containsWord(wordToRemove);
        assertFalse(isInUserDictionary, "addedtouser should be removed from the user dictionary.");
    }

}
