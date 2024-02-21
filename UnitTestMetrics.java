import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UnitTestMetrics {

    // UNIT TESTING: GET AND INCREASE FUNCTIONS IN METRICS CLASS --------------------
    /**
     * Test 1: testing that incrementing number of characters works correctly
     */
    @Test
    void testIncreaseCharacterCount() {
        Metrics metricsObj = new Metrics();
        metricsObj.numCharacters = 1;
        metricsObj.increaseCharacterCount(5);
        assertEquals(6, metricsObj.getNumCharacters());
        System.out.println("Number of characters : " + metricsObj.getNumCharacters());
    }

    /**
     * Test 2: testing that incrementing number of words works correctly
     */
    @Test
    void testIncreaseWordCount() {
        Metrics metricsObj = new Metrics();
        metricsObj.numWords = 1;
        metricsObj.increaseWordCount(5);
        assertEquals(6, metricsObj.getNumWords());
        System.out.println("Number of words : " + metricsObj.getNumWords());
    }

    /**
     * Test 3: testing that incrementing number of lines works correctly
     */
     @Test
    void testIncreaseLineCount() {
        Metrics metricsObj = new Metrics();
        metricsObj.numLines = 1;
        metricsObj.increaseLineCount(5);
        assertEquals(6, metricsObj.getNumLines());
        System.out.println("Number of lines : " + metricsObj.getNumLines());
    }

    /**
     * Test 4: testing that incrementing number of misspelling errors works correctly
     */
    @Test
    void testIncreaseMisspelling() {
        Metrics metricsObj = new Metrics();
        metricsObj.numMisspellingError = 1;
        metricsObj.increaseMisspelling(5);
        assertEquals(6, metricsObj.getNumMisspellingError());
        System.out.println("Number of missepelling errors : " + metricsObj.getNumMisspellingError());
    }

    /**
     * Test 5: testing that incrementing number of miscapitalization errors works correctly
     */ 
    @Test
    void testIncreaseMiscapitalization() {
        Metrics metricsObj = new Metrics();
        metricsObj.numMiscapitalizationError = 1;
        metricsObj.increaseMiscapitalization(5);
        assertEquals(6, metricsObj.getNumMiscapitalizationError());
        System.out.println("Number of miscapitalization errors : " + metricsObj.getNumMiscapitalizationError());
    }

    /**
     * Test 6: testing that incrementing number of double words errors works correctly
     */ 
    @Test
    void testIncreaseDoubleWord() {
        Metrics metricsObj = new Metrics();
        metricsObj.numDoubleWordsError = 1;
        metricsObj.increaseDoubleWord(5);
        assertEquals(6, metricsObj.getNumDoubleWordsError());
        System.out.println("Number of double words errors : " + metricsObj.getNumMiscapitalizationError());
    }

    /**
     * Test 7: testing that incrementing number of accepted suggestions works correctly
     */ 
    @Test
    void testIncreaseAcceptedSuggestions() {
        Metrics metricsObj = new Metrics();
        metricsObj.numAcceptedSuggestions = 1;
        metricsObj.increaseAcceptedSuggestions(5);
        assertEquals(6, metricsObj.getNumAcceptedSuggestions());
        System.out.println("Number of accepted suggestions : " + metricsObj.getNumAcceptedSuggestions());
    }

    /**
     * Test 8: testing that incrementing number of deletions works correctly
     */ 
    @Test
    void testIncreaseDeletions() {
        Metrics metricsObj = new Metrics();
        metricsObj.numDeletions = 1;
        metricsObj.increaseDeletions(5);
        assertEquals(6, metricsObj.getNumDoubleWordsError());
        System.out.println("Number of deletions : " + metricsObj.getNumMiscapitalizationError());
    }

    /**
     * Test 9: testing that incrementing number of manual corrections works correctly
     */
    @Test
    void testIncreaseManualCorrections() {
        Metrics metricsObj = new Metrics();
        metricsObj.numManualCorrections = 1;
        metricsObj.increaseManualCorrections(5);
        assertEquals(6, metricsObj.getNumManualCorrections());
        System.out.println("Number of manual corrections : " + metricsObj.getNumManualCorrections());
    }
    
    
    // INTEGRATION TESTING: THE DOCUMENT CLASS IS CORRECTLY COUNTING THE NUMBER OF CHARS, WORDS, AND LINES;
    // AND THE SPELLCHECKER CLASS IS COUNTING THE CORRECT NUMBER OF EACH ERROR TYPE FOR A TEST DOCUMENT
    /**
     * Test 1: retrieving the metric for the number of characters (EXCLUDING SPACES)
     * in a document
     */
    @Test
    void testGetNumCharacters() {
        Document doc = new Document("testDocument.txt", false);
        assertEquals(2336, doc.docMetrics.getNumCharacters());
        System.out.println("Number of characters : " + doc.docMetrics.getNumCharacters());
    }

    /**
     * Test 2: retrieving the metric for the number of words in a document
     */
    @Test
    void testGetNumWords() {
        Document doc = new Document("testDocument.txt", false);
        assertEquals(406, doc.docMetrics.getNumWords());
        System.out.println("Number of words: " + doc.docMetrics.getNumWords());
    }

    /**
     * Test 3: retrieving the metric for the number of lines in a document
     */
    @Test
    void testGetNumLines() {
        Document doc = new Document("testDocument.txt", false);
        assertEquals(19, doc.docMetrics.getNumLines());
        System.out.println("Number of lines: " + doc.docMetrics.getNumLines());
    }   
    
    /**
     * Test 4: retrieving the number of misspelling errors in a document
     */
    @Test
    void testGetNumMisspellingError() {
        SpellChecker mySpellChecker = new SpellChecker("testDocument.txt", false);
        assertEquals(202, mySpellChecker.myMetrics.getNumMisspellingError());
        System.out.println("Number of misspelling errors: " + mySpellChecker.myMetrics.getNumMisspellingError());
    }

    /**
     * Test 5: retrieving the number of miscapitalization errors in a document
     */
    @Test
    void testGetNumMiscapitalizationError() {
        SpellChecker mySpellChecker = new SpellChecker("testDocument.txt", false);
        assertEquals(1, mySpellChecker.myMetrics.getNumMiscapitalizationError());
        System.out.println("Number of misspelling errors: " + mySpellChecker.myMetrics.getNumMiscapitalizationError());
    }

    /**
     * Test 6: retrieving the number of double word errors in a document
     */
    @Test
    void testGetNumDoubleWordsError() {
        SpellChecker mySpellChecker = new SpellChecker("testDocument.txt", false);
        assertEquals(0, mySpellChecker.myMetrics.getNumDoubleWordsError());
        System.out.println("Number of misspelling errors: " + mySpellChecker.myMetrics.getNumDoubleWordsError());
    }

    /**
     * Test 7: retrieving the number of accepted suggestions (type of error correction)
     */
    @Test
    void testGetNumAcceptedSuggestions() {
        SpellChecker mySpellChecker = new SpellChecker("testDocument.txt", false);
        // Testing how the GUI increases the number of accepted suggestions by the user
        // This is how SpellCheckInterface.java keeps track of accepted suggestions using a SpellChecker object
        mySpellChecker.myMetrics.increaseAcceptedSuggestions(1);
        assertEquals(1, mySpellChecker.myMetrics.getNumAcceptedSuggestions());
        System.out.println("Number of Accepted Suggestions: " + mySpellChecker.myMetrics.getNumAcceptedSuggestions());
    }

    /**
     * Test 8: retrieving the number of deletions (type of error correction)
     */
    @Test
    void testGetNumDeletions() {
        SpellChecker mySpellChecker = new SpellChecker("testDocument.txt", false);
        // Testing how the GUI increases the number of deletions by the user
        // This is how SpellCheckInterface.java keeps track of deletions using a SpellChecker object
        mySpellChecker.myMetrics.increaseDeletions(2);
        assertEquals(2, mySpellChecker.myMetrics.getNumDeletions());
        System.out.println("Number of Deletions: " + mySpellChecker.myMetrics.getNumDeletions());
    }

    /**
     * Test 9: retrieving the number of manual corrections (type of error correction)
     */
    @Test
    void testGetNumManualCorrections() {
        SpellChecker mySpellChecker = new SpellChecker("testDocument.txt", false);
        // Testing how the GUI increases the number of manual corrections by the user
        // This is how SpellCheckInterface.java keeps track of manual corrections using a SpellChecker object
        mySpellChecker.myMetrics.increaseManualCorrections(3);
        assertEquals(3, mySpellChecker.myMetrics.getNumManualCorrections());
        System.out.println("Number of Manual Corrections: " + mySpellChecker.myMetrics.getNumManualCorrections());
    }


}