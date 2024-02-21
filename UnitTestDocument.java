import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class UnitTestDocument {
  // Test 1: testing .txt document word list instantiation
  @Test
  void testTxtInstantiation() {
    Document doc = new Document("testDocumentShort.txt", false);
    assertNotEquals(0, doc.getWordsList().size());
  }

  // Test 2: testing correctness for document's words list
  @Test
  void testCorrectWords() {
    Document doc = new Document("testDocumentShort.txt", false);
    assertEquals("this", doc.getWordsList().get(0)); // first word
    assertEquals("document.", doc.getWordsList().get(4)); // middle word
    assertEquals("1234", doc.getWordsList().get(6)); // last word
  }

  // Test 3: testing doc filepath retrieval
  @Test
  void testGetFilePath() {
    Document doc = new Document("testDocumentShort.txt", false);
    assertEquals("testDocumentShort.txt", doc.getFilePath());
  }

  // Test 4: testing doc getContextBefore
  @Test
  void testGetContextBefore() {
    Document doc = new Document("testDocument.txt", false);
    String correctContext = "sodales lorem semper a. Sed suscipit libero at ex suscipit, vel condimentum mi dignissim. Ut mollis ullamcorper egestas. Aenean pulvinar euismod ante, eget rhoncus odio dictum at. Aenean molestie vulputate tincidunt. Quisque in ex metus. Fusce blandit ultricies odio sit amet posuere. \n \n Vestibulum blandit porttitor ex volutpat luctus.";
    assertEquals(correctContext, doc.getContextBefore(247));

    doc = new Document("testDocumentShort.txt", false); // testing context on very short document
    correctContext = "this is a test";
    assertEquals(correctContext, doc.getContextBefore(4));
    }

  // Test 5: testing doc getContextBefore for the many-line-breaks edgecase
  @Test
  void testGetContextBeforeWLineBreaks() {
    Document doc = new Document("testDocument.txt", false);
    String correctContext = "\n \n \n welcome! \n \n Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean egestas nulla";
    assertEquals(correctContext, doc.getContextBefore(50));
  }

  // Test 6: testing doc getContextafter
  @Test
  void testGetContextAfter() {
    Document doc = new Document("testDocument.txt", false);
    String correctContext = "sit amet tincidunt ex lobortis vel. Etiam pretium sem vel turpis pharetra euismod. Morbi rutrum commodo velit in mattis. Mauris posuere luctus felis eget suscipit. Suspendisse malesuada orci eu dictum cursus. Duis bibendum eu ante ut condimentum. Aliquam erat volutpat. Vivamus elit metus, auctor sit amet viverra nec, consequat";
    assertEquals(correctContext, doc.getContextAfter(250));

    doc = new Document("testDocumentShort.txt", false); // testing context on very short document
    correctContext = "testing 1234 \n";
    assertEquals(correctContext, doc.getContextAfter(4));
  }

  // Test 7: testing replacing word in word list
  @Test
  void testReplaceWord() {
    Document doc = new Document("testDocument.txt", false);
    String newWord = String.valueOf((int) Math.floor(Math.random()*1000000));
    doc.replaceWord(1, newWord);
    assertEquals(newWord, doc.getWordsList().get(1));

    doc = new Document("testDocument.txt", false); // replace word that includes punctuation
    newWord = String.valueOf((int) Math.floor(Math.random()*1000000));
    doc.replaceWord(4, newWord);
    assertEquals("!!"+newWord+"?.", doc.getWordsList().get(4));
  }

  // Test 8: testing overwriting the file
  @Test
  void testOverwrite() {
    Document doc = new Document("testDocumentSaved.txt", false);
    String newWord = String.valueOf((int) Math.floor(Math.random()*1000000));
    doc.replaceWord(0, newWord);
    doc.replaceWord(1, newWord);
    doc.overwriteFile(doc.getWordsList());
    Document savedDoc = new Document("testDocumentSaved.txt", false);
    assertEquals(newWord, savedDoc.getWordsList().get(0));
    assertEquals(newWord, savedDoc.getWordsList().get(1));
  }

  // Test 9: testing saving the file as new file
  @Test
  void testSaveNew() {
    Document doc = new Document("testDocumentSaved.txt", false);
    String newWord = String.valueOf((int) Math.floor(Math.random()*1000000));
    doc.replaceWord(1, newWord);
    String newFilePath = "testDocumentSaved2.txt";
    doc.saveNewFile(doc.getWordsList(), newFilePath);
    Document savedDoc = new Document(newFilePath, false);
    assertEquals(newWord, savedDoc.getWordsList().get(1));
  }

  // Test 10: testing .html or .xml word list instantiation
  @Test
  void testHTMLInstantiation() {
    Document htmlDoc = new Document("testHTML.html", true);
    assertNotEquals(0, htmlDoc.getWordsList().size());
  }

  // Test 11: testing .html or .xml word list instantiation, everything in <> tags should be passed as single word
  @Test
  void testHTMLTagsWordsList() {
    Document htmlDoc = new Document("testHTML.html", true);
    for (String currWord : htmlDoc.getWordsList()) { //every 'word' should either be a tag (start with < and end with >) or be a real word
        assertTrue((currWord.startsWith("<") && currWord.endsWith(">")) || (!(currWord.startsWith("<")) && !(currWord.endsWith(">"))));
    }
  }
}
