import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UnitTestEnglishDict {
    private static EnglishDictionary englishDictionary;

    // Initialize the EnglishDictionary with a test file before running tests
    @BeforeAll
    static void setUp() {
        englishDictionary = new EnglishDictionary();
    }
    
    // Test 1: Check if dictionary is not empty after initialization
    @Test
    void testDictNotEmpty() {
        assertFalse(englishDictionary.englishDictionary.isEmpty());
    }

    // Test 2: Check if a known word is in the dictionary
    @Test
    void testKnownWord() {
        assertTrue(englishDictionary.containsWord("addleplot"));
    }

     // Test 3: Check if a non-existent word is correctly identified as not in dictionary
     @Test
     void testNotKnownWord() {
         assertFalse(englishDictionary.containsWord("addong"));
     }

     // Test 4: Test case insensitivity
    @Test
    void testCaseInsensitivity() {
        assertTrue(englishDictionary.containsWord("ApPlE"));
    }


    
}
