import static org.junit.Assert.*;
import org.junit.Test;

public class UnitTestUserDict {

    @Test
    public void testAddWord() {
        UserDictionary userDict = new UserDictionary();

        userDict.addWord("testWord");
        boolean containsWord = userDict.containsWord("testWord");

        if (containsWord) {
            System.out.println("Test 'testAddWord' passed: Word successfully added to the dictionary.");
        } else {
            System.out.println("Test 'testAddWord' failed: Word addition failed or word not found.");
        }

        assertTrue(containsWord);
    }

    @Test
    public void testRemoveWord() {
        UserDictionary userDict = new UserDictionary();

        userDict.addWord("testWord");
        userDict.removeWord("testWord");
        boolean containsWord = userDict.containsWord("testWord");

        if (!containsWord) {
            System.out.println("Test 'testRemoveWord' passed: Word successfully removed from the dictionary.");
        } else {
            System.out.println("Test 'testRemoveWord' failed: Word removal failed or word still present.");
        }

        assertFalse(containsWord);
    }

    @Test
    public void testEditWord() {
        UserDictionary userDict = new UserDictionary();

        userDict.addWord("testWord");
        userDict.editWord("testWord", "editedWord");
        boolean originalWordPresent = userDict.containsWord("testWord");
        boolean editedWordPresent = userDict.containsWord("editedWord");

        if (!originalWordPresent && editedWordPresent) {
            System.out.println("Test 'testEditWord' passed: Word successfully edited in the dictionary.");
        } else {
            System.out.println("Test 'testEditWord' failed: Word edit failed or words not found.");
        }

        assertFalse(originalWordPresent);
        assertTrue(editedWordPresent);
    }

    @Test
    public void testResetDictionary() {
        UserDictionary userDict = new UserDictionary();

        userDict.addWord("testWord");
        userDict.resetDictionary();
        boolean containsWord = userDict.containsWord("testWord");

        if (!containsWord) {
            System.out.println("Test 'testResetDictionary' passed: Dictionary reset successfully.");
        } else {
            System.out.println("Test 'testResetDictionary' failed: Dictionary reset failed or word still present.");
        }

        assertFalse(containsWord);
    }
}
