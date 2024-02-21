import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The SpellCheckInterface class is used to spell check a file using a GUI.
 * Users can open a txt/html/xml file, find spelling errors in these files, 
 * correct spelling errors, view file metrics, save the new file, edit the
 * user dictionary, view help for how to use the program, and change the 
 * color scheme of the application in a user-friendly interface. 
 * 
 * @author      Joud Adel Al-Lahham <jallahha@uwo.ca>
 * @author      Jodi Keizer <jkeizer@uwo.ca>
 * @author      Anna Ma <ama92@uwo.ca>
 * @author      Ivan Quan <iquan5@uwo.ca>
 * @author      Kevin Xie <kxie49@uwo.ca>
 * @version     1.0
 */

public class SpellCheckInterface {
    private static File myFile;
    private static boolean isHTML = false;
    public static SpellChecker mySpellChecker;
    private static Configuration myConfig = new Configuration();
    private static UserDictionary myUserDictionary;
    private static int userDictIndex = 0;
    private static String userDictPath = "user_dict.txt";

    /**
     * Setter for myFile, sets myFile to a selected file
     *
     * @param selectedFile the file to set myFile with
     */
    public static void setFile(File selectedFile) {
        myFile = selectedFile;
    }

    /**
     * Initialized the selected document, initializes myDocument 
     * to a new Document object
     *
     * @param selectedFile the file to initialize myDocument with
     * @param isHTML true for html/xml file, false for txt file
     */
    public static void initSpellChecker(File selectedFile, boolean isHTML, String userDictPath) {
        mySpellChecker = new SpellChecker(selectedFile.getPath(), isHTML);
    }

    /**
     * main is run to start the GUI and run the spell checker application.
     * It opens the GUI, and loads it with the necessary interface to 
     * spell check a file.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame(); // all content will be displayed on this frame
        frame.setSize(1200,680); // set frame width and height
        CardLayout cardLayout = new CardLayout();
        JPanel mainCards = new JPanel(cardLayout); // cards are used to switch between screens (main menu, user dictionary, spell checker, metrics, file save confirm screen)

        // TOP LOGO BAR (always displayed)
        JPanel logoPanel = new JPanel(new GridBagLayout()); // this panel holds the bar at the top of the screen (contains logo) (always displayed)
        logoPanel.setPreferredSize(new Dimension(Short.MAX_VALUE, 90)); // set top bar height
        logoPanel.setBackground(Color.WHITE);
        frame.add(logoPanel, BorderLayout.NORTH); // add logo bar to top of frame
        JLabel logoTitle = new JLabel("SpellBuddy"); // make logo text
        Font font = logoTitle.getFont();
        logoTitle.setFont(new Font(font.getName(), font.getStyle(), 26)); // set logo text size
        logoTitle.setBorder(new EmptyBorder(0, 0, 0, 820)); // set left margin on text
        logoPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(222,222,222))); //make gray bottom border for top logo bar
        GridBagConstraints gbc = new GridBagConstraints(); // position file name text at top of spell check page
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        logoPanel.add(logoTitle, gbc); // add text to logo panel
        
        JButton helpButton = new JButton("Help"); // add help button to topbar display
        helpButton.setPreferredSize(new Dimension(100, 40));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        logoPanel.add(helpButton, gbc);
        helpButton.addActionListener(new ActionListener() { // open help dialog upon clicking help button
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showHelpDialog(frame);
                }
                catch(Exception exc) {
                    exc.printStackTrace();
                }
            }
        });


        // USER DICTIONARY SCREEN
        JPanel userDictPage = new JPanel(); // make user dictionary screen
        userDictPage.setBackground(Color.WHITE);
        userDictPage.setLayout(new GridBagLayout()); // use grid bag layout on user dict check screen

        JPanel userDictMain = new JPanel(new GridBagLayout()); // make the container (panel) to hold user dictionary and its options
        userDictMain.setPreferredSize(new Dimension(980, 440)); // set size
        userDictMain.setBackground(new Color(240,240,240));
        gbc.insets = new Insets(5, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        userDictPage.add(userDictMain, gbc); // add spell check main to spell check page

        JTextPane userDictWords = new JTextPane(); // represents the words in the user dictionary
        String dictInitialText = "File read failed. Please exit and try again."; // text will be changed upon reading user dict. if read fails, this error is shown
        userDictWords.setText(dictInitialText);
        userDictWords.setPreferredSize(new Dimension(900, 260));
        userDictWords.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        userDictMain.add(userDictWords, gbc); 

        JLabel currentDictWord = new JLabel(); // contains the current word in user dictionary
        currentDictWord.setText("Error: ".concat("failed to retrieve word"));
        font = currentDictWord.getFont();
        currentDictWord.setFont(new Font(font.getName(), font.getStyle(), 15)); 
        currentDictWord.setBorder(new EmptyBorder(0, 0, 7, 0)); // set bottom margin on text
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        userDictMain.add(currentDictWord, gbc); 

        JLabel dictOptionsText = new JLabel("Options:"); // the "Options:" text
        font = dictOptionsText.getFont();
        dictOptionsText.setFont(new Font(font.getName(), font.getStyle(), 15)); 
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTH;
        userDictMain.add(dictOptionsText, gbc); 
        
        JPanel userDictButtonsContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 70, 2)); // panel contains word suggestion buttons
        userDictButtonsContainer.setPreferredSize(new Dimension(920, 60));
        userDictButtonsContainer.setBackground(new Color(240,240,240));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.SOUTH;
        userDictMain.add(userDictButtonsContainer, gbc); 

        Dimension userDictButtonDimensions = new Dimension(160, 50);
        JButton userDictButton1, userDictButton2, userDictButton3, userDictButton4;

        userDictButton1 = new JButton(); // a button to add word to user dict
        userDictButton1.setText("Add");
        userDictButton1.setPreferredSize(userDictButtonDimensions);
        userDictButtonsContainer.add(userDictButton1);

        userDictButton2 = new JButton(); // a button to edit word in user dict
        userDictButton2.setText("Edit");
        userDictButton2.setPreferredSize(userDictButtonDimensions);
        userDictButtonsContainer.add(userDictButton2);

        userDictButton3 = new JButton(); // a button to remove word in user dict
        userDictButton3.setText("Remove");
        userDictButton3.setPreferredSize(userDictButtonDimensions);
        userDictButtonsContainer.add(userDictButton3);
        
        userDictButton4 = new JButton(); // a button to go next in user dict
        userDictButton4.setText("Next");
        userDictButton4.setPreferredSize(userDictButtonDimensions);
        userDictButtonsContainer.add(userDictButton4);

        ActionListener nextDictWord = new ActionListener() { // go to next word in dicitonary upon button press
            @Override
            public void actionPerformed(ActionEvent e) {
                if(userDictIndex+1 < myUserDictionary.userDictToArrayList().size()) userDictIndex++;
                updateUserDictText(userDictWords, currentDictWord);
            }
        };
        userDictButton4.addActionListener(nextDictWord);

        ActionListener addWord = new ActionListener() { // adds add word function to add word button
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = JOptionPane.showInputDialog("Add a word:"); 
                if(!(userInput==null)){ // if user entered input, add word to user dict
                    myUserDictionary.addWord(userInput.toLowerCase());
                    updateUserDictText(userDictWords, currentDictWord);
                }
            }
        };
        userDictButton1.addActionListener(addWord);

        ActionListener editWord = new ActionListener() { // adds edit word function to edit word button
            @Override
            public void actionPerformed(ActionEvent e) {
                String currDictWord = myUserDictionary.userDictToArrayList().get(userDictIndex);
                String userInput = JOptionPane.showInputDialog("Edit word: "+currDictWord, currDictWord);
                if(!(userInput==null)){ // if user entered input, replace word in user dict
                    myUserDictionary.editWord(currDictWord, userInput);
                    updateUserDictText(userDictWords, currentDictWord);
                }
            }
        };
        userDictButton2.addActionListener(editWord);

        ActionListener removeWord = new ActionListener() { // adds remove word function to remove word button
            @Override
            public void actionPerformed(ActionEvent e) {
                String currDictWord = myUserDictionary.userDictToArrayList().get(userDictIndex);
                myUserDictionary.removeWord(currDictWord);
                updateUserDictText(userDictWords, currentDictWord);
            }
        };
        userDictButton3.addActionListener(removeWord);

        JPanel bottomDictPanel = new JPanel(new BorderLayout()); // this container contains the "done" and "reset dictionary" buttons
        bottomDictPanel.setBackground(Color.WHITE);
        gbc.gridx = 6;
        gbc.gridy = 1;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;
        userDictPage.add(bottomDictPanel, gbc);

        JButton doneDictButton = new JButton("Done"); // done user dict button to proceed to main menu
        doneDictButton.setPreferredSize(new Dimension(100, 50));
        bottomDictPanel.add(doneDictButton, BorderLayout.EAST);
        doneDictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you are done editing the user dictionary?", "Confirmation", JOptionPane.YES_NO_OPTION); // confirmation panel for finishing edit user dict
                if (!(confirmation == JOptionPane.YES_OPTION)) {
                    return;
                }
                int result = JOptionPane.showConfirmDialog(frame, "Do you want to save your changes to the user dictionary?", "Confirmation", JOptionPane.YES_NO_OPTION); // confirmation panel for saving changes
                if (result == JOptionPane.YES_OPTION) {
                    myUserDictionary.saveDictionary("user_dict.txt");
                    cardLayout.show(mainCards, "menuScreen"); // advance to main menu
                    return;
                }
                else if (result == JOptionPane.NO_OPTION) {
                    cardLayout.show(mainCards, "menuScreen"); // advance to main menu without saving
                    return;
                }
                else {
                    return;
                }
            }
        });

        JButton resetDictButton = new JButton("Reset User Dictionary"); // button to reset user dict
        resetDictButton.setPreferredSize(new Dimension(180, 50));
        bottomDictPanel.add(resetDictButton, BorderLayout.WEST);
        resetDictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to reset the user dictionary?", "Confirmation", JOptionPane.YES_NO_OPTION); // confirmation panel for finishing spellchecking
                if (!(result == JOptionPane.YES_OPTION)) {
                    return;
                }
                myUserDictionary.resetDictionary();
                myUserDictionary.saveDictionary("user_dict.txt");
                cardLayout.show(mainCards, "menuScreen"); 
            }
        });


        // METRICS SCREEN
        JPanel metricsPage = new JPanel(); // create metrics page
        metricsPage.setBackground(Color.WHITE);
        metricsPage.setLayout(new GridBagLayout()); // use grid bag layout on spell check screen

        JLabel metricsText = new JLabel("Metrics"); // make file name text element
        font = metricsText.getFont();
        metricsText.setFont(new Font(font.getName(), font.getStyle(), 14)); // set font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        metricsPage.add(metricsText, gbc); // add file name text element to spell check page
        
        JPanel metricsMain = new JPanel(new GridBagLayout()); // make the container (panel) to hold text and correction options
        metricsMain.setPreferredSize(new Dimension(980, 440)); // set size
        metricsMain.setBackground(new Color(240,240,240)); //position main panel under file text
        gbc.insets = new Insets(5, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        metricsPage.add(metricsMain, gbc); // add spell check main to spell check page

        JPanel documentMetricsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5)); // panel contains word suggestion buttons
        documentMetricsPanel.setPreferredSize(new Dimension(300, 320));
        documentMetricsPanel.setBackground(new Color(230,230,230));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        metricsMain.add(documentMetricsPanel, gbc); 

        JLabel charCountLabel = new JLabel("Character Count"); // the label for character count
        font = charCountLabel.getFont();
        charCountLabel.setFont(new Font(font.getName(), font.getStyle(), 14)); // set text size
        charCountLabel.setBorder(new EmptyBorder(30, 30, 0, 100)); // set left margin on text
        documentMetricsPanel.add(charCountLabel);
        
        JLabel charCountNumLabel = new JLabel("0"); // the number for character count
        font = charCountNumLabel.getFont();
        charCountNumLabel.setFont(new Font(font.getName(), font.getStyle(), 19)); // set text size
        charCountNumLabel.setBorder(new EmptyBorder(0, 30, 0, 100)); // set left margin on text
        documentMetricsPanel.add(charCountNumLabel);      
        
        JLabel wordCountLabel = new JLabel("Word Count"); // the label for word count
        font = wordCountLabel.getFont();
        wordCountLabel.setFont(new Font(font.getName(), font.getStyle(), 14)); // set text size
        wordCountLabel.setBorder(new EmptyBorder(30, 30, 0, 100)); // set left margin on text
        documentMetricsPanel.add(wordCountLabel);        
        
        JLabel wordCountNumLabel = new JLabel("0"); // the number for word count
        font = wordCountNumLabel.getFont();
        wordCountNumLabel.setFont(new Font(font.getName(), font.getStyle(), 19)); // set text size
        wordCountNumLabel.setBorder(new EmptyBorder(0, 30, 0, 100)); // set left margin on text
        documentMetricsPanel.add(wordCountNumLabel);      

        JLabel lineCountLabel = new JLabel("Line Count"); // the label for line count
        font = lineCountLabel.getFont();
        lineCountLabel.setFont(new Font(font.getName(), font.getStyle(), 14)); // set text size
        lineCountLabel.setBorder(new EmptyBorder(30, 30, 0, 100)); // set left margin on text
        documentMetricsPanel.add(lineCountLabel);     
        
        JLabel lineCountNumLabel = new JLabel("0"); // the number for line count
        font = lineCountNumLabel.getFont();
        lineCountNumLabel.setFont(new Font(font.getName(), font.getStyle(), 19)); // set text size
        lineCountNumLabel.setBorder(new EmptyBorder(0, 30, 0, 100)); // set left margin on text
        documentMetricsPanel.add(lineCountNumLabel);         

        JPanel misspellingsMetricsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5)); // panel contains word suggestion buttons
        misspellingsMetricsPanel.setPreferredSize(new Dimension(300, 320));
        misspellingsMetricsPanel.setBackground(new Color(230,230,230));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        metricsMain.add(misspellingsMetricsPanel, gbc); 

        JLabel misspelledCountLabel = new JLabel("Misspelled Words"); // the label for misspelled words
        font = misspelledCountLabel.getFont();
        misspelledCountLabel.setFont(new Font(font.getName(), font.getStyle(), 14)); // set text size
        misspelledCountLabel.setBorder(new EmptyBorder(30, 30, 0, 100)); // set left margin on text
        misspellingsMetricsPanel.add(misspelledCountLabel);
        
        JLabel misspelledCountNumLabel = new JLabel("0"); // the number for misspelled count 
        font = misspelledCountNumLabel.getFont();
        misspelledCountNumLabel.setFont(new Font(font.getName(), font.getStyle(), 19)); // set text size
        misspelledCountNumLabel.setBorder(new EmptyBorder(0, 30, 0, 100)); // set left margin on text
        misspellingsMetricsPanel.add(misspelledCountNumLabel);   

        JLabel miscapitalizedCountLabel = new JLabel("Miscapitalized Words"); // the label for miscapitalized words
        font = miscapitalizedCountLabel.getFont();
        miscapitalizedCountLabel.setFont(new Font(font.getName(), font.getStyle(), 14)); // set text size
        miscapitalizedCountLabel.setBorder(new EmptyBorder(30, 30, 0, 100)); // set left margin on text
        misspellingsMetricsPanel.add(miscapitalizedCountLabel);
        
        JLabel miscapitalizedCountNumLabel = new JLabel("0"); // the number for miscapitalized count
        font = miscapitalizedCountNumLabel.getFont();
        miscapitalizedCountNumLabel.setFont(new Font(font.getName(), font.getStyle(), 19)); // set text size
        miscapitalizedCountNumLabel.setBorder(new EmptyBorder(0, 30, 0, 100)); // set left margin on text
        misspellingsMetricsPanel.add(miscapitalizedCountNumLabel);  

        JLabel doubleWordCountLabel = new JLabel("Double Word Errors"); // the label for double words
        font = doubleWordCountLabel.getFont();
        doubleWordCountLabel.setFont(new Font(font.getName(), font.getStyle(), 14)); // set text size
        doubleWordCountLabel.setBorder(new EmptyBorder(30, 30, 0, 100)); // set left margin on text
        misspellingsMetricsPanel.add(doubleWordCountLabel);
        
        JLabel doubleWordCountNumLabel = new JLabel("0"); // the number for double words 
        font = doubleWordCountNumLabel.getFont();
        doubleWordCountNumLabel.setFont(new Font(font.getName(), font.getStyle(), 19)); // set text size
        doubleWordCountNumLabel.setBorder(new EmptyBorder(0, 30, 0, 100)); // set left margin on text
        misspellingsMetricsPanel.add(doubleWordCountNumLabel);  
        
        JPanel changesMetricsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5)); // panel contains word suggestion buttons
        changesMetricsPanel.setPreferredSize(new Dimension(300, 320));
        changesMetricsPanel.setBackground(new Color(230,230,230));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        metricsMain.add(changesMetricsPanel, gbc); 
        
        JLabel acceptedCountLabel = new JLabel("Accepted Suggestions"); // the label for accepted suggestions
        font = acceptedCountLabel.getFont();
        acceptedCountLabel.setFont(new Font(font.getName(), font.getStyle(), 14)); // set text size
        acceptedCountLabel.setBorder(new EmptyBorder(30, 30, 0, 100)); // set left margin on text
        changesMetricsPanel.add(acceptedCountLabel);
        
        JLabel acceptedCountNumLabel = new JLabel("0"); 
        font = acceptedCountNumLabel.getFont();
        acceptedCountNumLabel.setFont(new Font(font.getName(), font.getStyle(), 19)); // set text size
        acceptedCountNumLabel.setBorder(new EmptyBorder(0, 30, 0, 100)); // set left margin on text
        changesMetricsPanel.add(acceptedCountNumLabel);  
        
        JLabel deletionsCountLabel = new JLabel("Number of Deletions"); // the label for deletions
        font = deletionsCountLabel.getFont();
        deletionsCountLabel.setFont(new Font(font.getName(), font.getStyle(), 14)); // set text size
        deletionsCountLabel.setBorder(new EmptyBorder(30, 30, 0, 100)); // set left margin on text
        changesMetricsPanel.add(deletionsCountLabel);
        
        JLabel deletionsCountNumLabel = new JLabel("0"); // the number for num deletions 
        font = deletionsCountNumLabel.getFont();
        deletionsCountNumLabel.setFont(new Font(font.getName(), font.getStyle(), 19)); // set text size
        deletionsCountNumLabel.setBorder(new EmptyBorder(0, 30, 0, 100)); // set left margin on text
        changesMetricsPanel.add(deletionsCountNumLabel);  

        JLabel manualCountLabel = new JLabel("Number of Manual Corrections"); // the label for manual corrections
        font = manualCountLabel.getFont();
        manualCountLabel.setFont(new Font(font.getName(), font.getStyle(), 14)); // set text size
        manualCountLabel.setBorder(new EmptyBorder(30, 30, 0, 100)); // set left margin on text
        changesMetricsPanel.add(manualCountLabel);
        
        JLabel manualCountNumLabel = new JLabel("0"); // the number for num manual corrections 
        font = manualCountNumLabel.getFont();
        manualCountNumLabel.setFont(new Font(font.getName(), font.getStyle(), 19)); // set text size
        manualCountNumLabel.setBorder(new EmptyBorder(0, 30, 0, 100)); // set left margin on text
        changesMetricsPanel.add(manualCountNumLabel);  
           
        Dimension metricsButtonDimension = new Dimension(120, 50);
        JButton saveButton, saveAsButton, exitButton;

        saveButton = new JButton(); // button to save and overwrite file
        saveButton.setText("Save and Overwrite");
        saveButton.setPreferredSize(metricsButtonDimension);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        metricsMain.add(saveButton, gbc);
        saveButton.addActionListener(new ActionListener() { // overwrite the file
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to overwrite?", "Confirmation", JOptionPane.YES_NO_OPTION); // confirmation panel for overwriting file
                if (!(result == JOptionPane.YES_OPTION)) {
                    return;
                }
                mySpellChecker.getDocument().overwriteFile(mySpellChecker.getDocument().getWordsList());
                System.out.println("Saved to: " + myFile.getPath());
                cardLayout.show(mainCards, "successScreen"); // advance to success screen
            }
        });

        saveAsButton = new JButton(); // button to save new file
        saveAsButton.setText("Save As");
        saveAsButton.setPreferredSize(metricsButtonDimension);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        metricsMain.add(saveAsButton, gbc);;
        JFileChooser saveFileChooser = new JFileChooser();
        saveFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        saveFileChooser.setCurrentDirectory(myFile); // set initial directory to file's directory
        saveAsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // save new file
                saveFileChooser.setCurrentDirectory(myFile); // set initial directory to file's directory
                String newFileName = JOptionPane.showInputDialog("New file name:", myFile.getName());
                if(newFileName == null) { // if user clicked cancel, return to metrics page
                    return;
                }
                int result = saveFileChooser.showOpenDialog(null); // pop up file selection box
                if(result == JFileChooser.CANCEL_OPTION) {  // if user clicked cancel, return to metrics page
                    return;
                }
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedDirectory = saveFileChooser.getSelectedFile();
                    String directoryPath = selectedDirectory.getAbsolutePath();
                    
                    File newFile = new File(directoryPath, newFileName);
                    String newFilePath = newFile.getAbsolutePath(); // get new file path

                    System.out.println("Selected file path: " + newFilePath);
                    mySpellChecker.getDocument().saveNewFile(mySpellChecker.getDocument().getWordsList(), newFilePath); // save new file in selected location
                    System.out.println("Saved to: " + newFilePath);
                }
                cardLayout.show(mainCards, "successScreen"); // advance to success screen
            }
        });

        exitButton = new JButton(); // button to return without saving
        exitButton.setText("Exit Without Saving");
        exitButton.setPreferredSize(metricsButtonDimension);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        metricsMain.add(exitButton, gbc);
        exitButton.addActionListener(new ActionListener() { // add exit funcitonality
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Confirmation", JOptionPane.YES_NO_OPTION); // confirmation panel for finishing spellchecking
                if (!(result == JOptionPane.YES_OPTION)) {
                    return;
                }
                cardLayout.show(mainCards, "menuScreen"); // return to main menu
            }
        });


        // SPELL CHECK SCREEN
        JPanel spellCheckPage = new JPanel(); // make spell check screen
        spellCheckPage.setBackground(Color.WHITE);
        spellCheckPage.setLayout(new GridBagLayout()); // use grid bag layout on spell check screen
        
        JLabel fileNameText = new JLabel(); // make file name text element
        font = fileNameText.getFont();
        fileNameText.setFont(new Font(font.getName(), font.getStyle(), 14)); // set font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        spellCheckPage.add(fileNameText, gbc); // add file name text element to spell check page

        JPanel spellCheckMain = new JPanel(new GridBagLayout()); // make the container (panel) to hold text and correction options
        spellCheckMain.setPreferredSize(new Dimension(980, 440)); // set size
        spellCheckMain.setBackground(new Color(240,240,240)); //position main panel under file text
        gbc.insets = new Insets(5, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        spellCheckPage.add(spellCheckMain, gbc); // add spell check main to spell check page

        JTextPane readWords = new JTextPane(); // represents the context surrounding the error
        String initialText = "File read failed. Please exit and try again."; // text will be changed upon reading file. if no file is read, this error is shown
        readWords.setText(initialText);
        readWords.setPreferredSize(new Dimension(900, 180));
        readWords.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        spellCheckMain.add(readWords, gbc); 

        JLabel currentWord = new JLabel(); // contains the current error being corrected
        currentWord.setText("Error: ".concat("failed to retrieve word"));
        font = currentWord.getFont();
        currentWord.setFont(new Font(font.getName(), font.getStyle(), 15)); 
        currentWord.setBorder(new EmptyBorder(0, 0, 7, 0)); // set bottom margin on text
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        spellCheckMain.add(currentWord, gbc); 

        JLabel suggestionsText = new JLabel("Suggestions:"); // the "Suggestions:" text
        font = suggestionsText.getFont();
        suggestionsText.setFont(new Font(font.getName(), font.getStyle(), 15)); 
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTH;
        spellCheckMain.add(suggestionsText, gbc); 

        JPanel suggestionsButtonsContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 2)); // panel contains word suggestion buttons
        suggestionsButtonsContainer.setPreferredSize(new Dimension(920, 60));
        suggestionsButtonsContainer.setBackground(new Color(240,240,240));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.SOUTH;
        spellCheckMain.add(suggestionsButtonsContainer, gbc); 

        Dimension spellCheckButtonDimensions = new Dimension(160, 50);
        String defaultSuggestion = "N/A";
        JButton suggest1, suggest2, suggest3, suggest4, suggest5;

        suggest1 = new JButton(); // a button containing a suggested spelling correction
        suggest1.setText(defaultSuggestion);
        suggest1.setPreferredSize(spellCheckButtonDimensions);
        suggestionsButtonsContainer.add(suggest1);

        suggest2 = new JButton(); // a button containing a suggested spelling correction
        suggest2.setText(defaultSuggestion);
        suggest2.setPreferredSize(spellCheckButtonDimensions);
        suggestionsButtonsContainer.add(suggest2);

        suggest3 = new JButton(); // a button containing a suggested spelling correction
        suggest3.setText(defaultSuggestion);
        suggest3.setPreferredSize(spellCheckButtonDimensions);
        suggestionsButtonsContainer.add(suggest3);
        
        suggest4 = new JButton(); // a button containing a suggested spelling correction
        suggest4.setText(defaultSuggestion);
        suggest4.setPreferredSize(spellCheckButtonDimensions);
        suggestionsButtonsContainer.add(suggest4);
        
        suggest5 = new JButton(); // a button containing a suggested spelling correction
        suggest5.setText(defaultSuggestion);
        suggest5.setPreferredSize(spellCheckButtonDimensions);
        suggestionsButtonsContainer.add(suggest5);
        
        ActionListener goNextWordListener = new ActionListener() { // updates text to get next error word upon clicking button
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean buttonIsNA = ((JButton) e.getSource()).getText().contains("N/A");
                if(!buttonIsNA) {
                    mySpellChecker.advanceNextError();
                    updateTextNextError(mySpellChecker.getCurrentWordIndex(), readWords, currentWord, suggest1, suggest2, suggest3, suggest4, suggest5);
                }
            }
        };
        suggest1.addActionListener(goNextWordListener); // add functionality for going to next error upon button press to all suggestion buttons
        suggest2.addActionListener(goNextWordListener);
        suggest3.addActionListener(goNextWordListener);
        suggest4.addActionListener(goNextWordListener);
        suggest5.addActionListener(goNextWordListener);

        ActionListener acceptSuggestionFromButton = new ActionListener() { // accepts suggestion and increments suggestion acceptance counter if button is not N/A
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonText = ((JButton) e.getSource()).getText();
                boolean buttonIsNA = buttonText.contains("N/A");
                if(!buttonIsNA) {
                    mySpellChecker.getDocument().replaceWord(mySpellChecker.getCurrentWordIndex(), buttonText);
                    mySpellChecker.myMetrics.increaseAcceptedSuggestions(1);
                    acceptedCountNumLabel.setText(Integer.toString(mySpellChecker.myMetrics.getNumAcceptedSuggestions()));
                }
            }
        };
        suggest1.addActionListener(acceptSuggestionFromButton); // add functionality for incrementing accepted metric
        suggest2.addActionListener(acceptSuggestionFromButton);
        suggest3.addActionListener(acceptSuggestionFromButton);
        suggest4.addActionListener(acceptSuggestionFromButton);
        suggest5.addActionListener(acceptSuggestionFromButton);

        JLabel optionsText = new JLabel("Options:"); // the "Options:" text
        font = optionsText.getFont();
        optionsText.setFont(new Font(font.getName(), font.getStyle(), 15)); 
        optionsText.setBorder(new EmptyBorder(7, 0, 0, 0)); // set top margin on text
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.SOUTH;
        spellCheckMain.add(optionsText, gbc); 
        
        JPanel optionsButtonsContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 2)); // panel contains options buttons
        optionsButtonsContainer.setPreferredSize(new Dimension(920, 60));
        optionsButtonsContainer.setBackground(new Color(240,240,240));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.SOUTH;
        spellCheckMain.add(optionsButtonsContainer, gbc); 

        JButton option1, option2, option3, option4, option5;
        option1 = new JButton(); // the ignore once button
        option1.setText("Ignore Once");
        option1.setPreferredSize(spellCheckButtonDimensions);
        optionsButtonsContainer.add(option1);

        option2 = new JButton(); // the ignore all button
        option2.setText("Ignore All");
        option2.setPreferredSize(spellCheckButtonDimensions);
        optionsButtonsContainer.add(option2);

        option3 = new JButton(); // the add to dictionary button
        option3.setText("Add to Dictionary");
        option3.setPreferredSize(spellCheckButtonDimensions);
        optionsButtonsContainer.add(option3);
        
        option4 = new JButton(); // the del word button
        option4.setText("Delete Word");
        option4.setPreferredSize(spellCheckButtonDimensions);
        optionsButtonsContainer.add(option4);
        
        option5 = new JButton(); // the custom input button
        option5.setText("Custom Input");
        option5.setPreferredSize(spellCheckButtonDimensions);
        optionsButtonsContainer.add(option5);

        option1.addActionListener(goNextWordListener); // add go to next error functionality for all options buttons
        option2.addActionListener(goNextWordListener);
        option3.addActionListener(goNextWordListener);
        option4.addActionListener(goNextWordListener);
        option5.addActionListener(goNextWordListener);

        ActionListener ignoreAll = new ActionListener() { // adds delete word function to delete word
            @Override
            public void actionPerformed(ActionEvent e) {
                mySpellChecker.addToIgnoredWords(mySpellChecker.getDocument().getWordsList().get(mySpellChecker.getCurrentWordIndex())); // add word at current index to ignore list
            }
        };
        option2.addActionListener(ignoreAll);

        ActionListener addToDict = new ActionListener() { // adds delete word function to delete word
            @Override
            public void actionPerformed(ActionEvent e) {
                myUserDictionary.addWord(SpellChecker.stripPunctuation(mySpellChecker.getDocument().getWordsList().get(mySpellChecker.getCurrentWordIndex())));
                myUserDictionary.saveDictionary(userDictPath);
            }
        };
        option3.addActionListener(addToDict);

        ActionListener deleteWord = new ActionListener() { // adds delete word function to delete word
            @Override
            public void actionPerformed(ActionEvent e) {
                mySpellChecker.getDocument().replaceWord(mySpellChecker.getCurrentWordIndex(), "");
                mySpellChecker.myMetrics.increaseDeletions(1);
                deletionsCountNumLabel.setText(Integer.toString(mySpellChecker.myMetrics.getNumDeletions()));
            }
        };
        option4.addActionListener(deleteWord);

        ActionListener customInput = new ActionListener() { // adds custom input function to custom input button
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = JOptionPane.showInputDialog("Manually correct spelling:", SpellChecker.stripPunctuation(mySpellChecker.getDocument().getWordsList().get(mySpellChecker.getCurrentWordIndex())));
                if(!(userInput==null)){ // if user entered input, change the word to user's input
                    mySpellChecker.getDocument().replaceWord(mySpellChecker.getCurrentWordIndex(), userInput);

                    mySpellChecker.myMetrics.increaseManualCorrections(1); // increment num of manual corrections by 1
                    manualCountNumLabel.setText(Integer.toString(mySpellChecker.myMetrics.getNumManualCorrections())); // update num manual corrections for metrics page display 
                }
            }
        };
        option5.addActionListener(customInput);

        JPanel bottomSpellPanel = new JPanel(new BorderLayout()); // this container contains the "done" button for spell checking
        bottomSpellPanel.setBackground(Color.WHITE);
        gbc.gridx = 6;
        gbc.gridy = 2;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;
        spellCheckPage.add(bottomSpellPanel, gbc);

        JButton doneSpellButton = new JButton("Done"); // done spellchecking button to proceed to metrics
        doneSpellButton.setPreferredSize(new Dimension(100, 40));
        bottomSpellPanel.add(doneSpellButton, BorderLayout.EAST);
        doneSpellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(frame, "Are you sure you are finished?", "Confirmation", JOptionPane.YES_NO_OPTION); // confirmation panel for overwriting file
                if (!(result == JOptionPane.YES_OPTION)) {
                    return;
                }
                cardLayout.show(mainCards, "metricsScreen"); // advance to metrics screen
            }
        });


        // MAIN MENU SCREEN
        JPanel menuPage = new JPanel(new GridBagLayout()); // make main menu page
        menuPage.setBackground(Color.WHITE);
        
        JLabel welcomeText = new JLabel("Welcome to SpellBuddy!"); // the welcome header on main menu screen
        font = welcomeText.getFont();
        welcomeText.setFont(new Font(font.getName(), font.getStyle(), 26)); 
        welcomeText.setBorder(new EmptyBorder(0, 0, 20, 0)); // set bottom margin on text
        welcomeText.setHorizontalAlignment(SwingConstants.CENTER); // center the text
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        menuPage.add(welcomeText, gbc);

        JPanel mainMenuPanel = new JPanel( new GridBagLayout()); // this container contains the text and buttons for file selection 
        gbc.insets = new Insets(5, 10, 10, 10);
        mainMenuPanel.setPreferredSize(new Dimension(400, 340));
        mainMenuPanel.setBackground(new Color(240,240,240));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        menuPage.add(mainMenuPanel, gbc);

        JLabel fileText = new JLabel("Pick a file:"); // the "Pick a file: text"
        font = fileText.getFont();
        fileText.setFont(new Font(font.getName(), font.getStyle(), 14)); // set font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        mainMenuPanel.add(fileText, gbc); 

        JButton pickFileButton; // button to activate file selector
        pickFileButton = new JButton("Select your file");
        pickFileButton.setPreferredSize(new Dimension((int) pickFileButton.getPreferredSize().getWidth(), 40));
        JFileChooser fileChooser = new JFileChooser();
        pickFileButton.addActionListener(new ActionListener() { // add file picking functionality to file button
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(null); // pop up file selection box
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    java.io.File selectedFile = fileChooser.getSelectedFile(); 
                    setFile(selectedFile);
                    System.out.println(myFile.getPath());
                    fileText.setText("Pick a file: ".concat(myFile.getName())); // upon selecting file, edit label text to file name
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainMenuPanel.add(pickFileButton, gbc);

        JLabel fileTypeText = new JLabel("Choose your file type:"); // choose file type text
        font = fileTypeText.getFont();
        fileTypeText.setFont(new Font(font.getName(), font.getStyle(), 14)); 
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        mainMenuPanel.add(fileTypeText, gbc);

        JToggleButton txtButton, htmlButton, xmlButton; // these buttons are toggles, only 1 is active at a time, select filetype

        txtButton = new JToggleButton(".txt"); // the .txt button
        txtButton.setPreferredSize(new Dimension(80, 35));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        mainMenuPanel.add(txtButton, gbc);

        htmlButton = new JToggleButton(".html"); // the .html button
        htmlButton.setPreferredSize(new Dimension(80, 35));
        gbc.gridx = 1;
        gbc.gridy = 4;
        mainMenuPanel.add(htmlButton, gbc);

        xmlButton = new JToggleButton(".xml"); // the .xml button
        xmlButton.setPreferredSize(new Dimension(80, 35));
        gbc.gridx = 2;
        gbc.gridy = 4;
        mainMenuPanel.add(xmlButton, gbc);
        ButtonGroup fileButtonGroup = new ButtonGroup();
        fileButtonGroup.add(txtButton);
        fileButtonGroup.add(htmlButton);
        fileButtonGroup.add(xmlButton);
        ActionListener toggleListener = new ActionListener() { // adds toggle functionality to the filetype buttons
            @Override
            public void actionPerformed(ActionEvent e) {
                JToggleButton sourceButton = (JToggleButton) e.getSource();
                if (sourceButton.isSelected()) { // set selected button, deselect others
                    txtButton.setSelected(sourceButton == txtButton);
                    htmlButton.setSelected(sourceButton == htmlButton);
                    xmlButton.setSelected(sourceButton == xmlButton);
                    sourceButton.setForeground(Color.RED);
                    if (sourceButton != txtButton) {
                        txtButton.setForeground(Color.BLACK);
                    }
                    if (sourceButton != htmlButton) {
                        htmlButton.setForeground(Color.BLACK);
                    }
                    if (sourceButton != xmlButton) {
                        xmlButton.setForeground(Color.BLACK);
                    }
                }else {
                    sourceButton.setForeground(Color.BLACK);
                }
            }
        };
        txtButton.addActionListener(toggleListener); // add this toggle functionality to the buttons
        htmlButton.addActionListener(toggleListener);
        xmlButton.addActionListener(toggleListener);

        JButton uploadButton; // clicking this button (when file and type are selected) advances the user to spell checking page
        uploadButton = new JButton("Upload File");
        uploadButton.setPreferredSize(new Dimension((int) uploadButton.getPreferredSize().getWidth(), 40));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainMenuPanel.add(uploadButton, gbc);
        uploadButton.addActionListener(new ActionListener() { // advance to spell check only if file and type are selected. different functionality based on file type
            @Override
            public void actionPerformed(ActionEvent e) {
                if(myFile != null) { // if file is selected, we can proceed to the spell check screen
                    fileNameText.setText("File: ".concat(myFile.getName()));
                    if (txtButton.isSelected()) {
                        isHTML = false;
                        cardLayout.show(mainCards, "spellCheckScreen");
                    } else if (htmlButton.isSelected()) {
                        isHTML = true;
                        cardLayout.show(mainCards, "spellCheckScreen");
                    } else if (xmlButton.isSelected()) {
                        isHTML = true;
                        cardLayout.show(mainCards, "spellCheckScreen");
                    } else {
                        JOptionPane.showMessageDialog(frame,"Please select a file type.","Notice",JOptionPane.INFORMATION_MESSAGE);
                    }
                    if(mySpellChecker == null) {
                        initSpellChecker(myFile, isHTML, "");
                    }
                    else {
                        mySpellChecker.loadNewDocument(myFile.getPath(), isHTML);
                    }
                    wordCountNumLabel.setText(Integer.toString(mySpellChecker.myMetrics.getNumWords()));
                    charCountNumLabel.setText(Integer.toString(mySpellChecker.myMetrics.getNumCharacters()));
                    lineCountNumLabel.setText(Integer.toString(mySpellChecker.myMetrics.getNumLines()));

                    misspelledCountNumLabel.setText(Integer.toString(mySpellChecker.myMetrics.getNumMisspellingError()));
                    miscapitalizedCountNumLabel.setText(Integer.toString(mySpellChecker.myMetrics.getNumMiscapitalizationError()));
                    doubleWordCountNumLabel.setText(Integer.toString(mySpellChecker.myMetrics.getNumDoubleWordsError()));

                    updateTextNextError(mySpellChecker.getCurrentWordIndex(), readWords, currentWord, suggest1, suggest2, suggest3, suggest4, suggest5);
                } else {
                    JOptionPane.showMessageDialog(frame,"Please select a file.","Notice",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton userDictButton; // button takes user to the page to edit user dictionary
        userDictButton = new JButton("Edit User Dictionary");
        userDictButton.setPreferredSize(new Dimension((int) uploadButton.getPreferredSize().getWidth(), 40));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainMenuPanel.add(userDictButton, gbc);
        userDictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myUserDictionary = new UserDictionary();
                myUserDictionary.loadDictionary(userDictPath);
                userDictIndex = 0;
                updateUserDictText(userDictWords, currentDictWord);
                cardLayout.show(mainCards, "userDictScreen");
            }
        });

        JButton invertButton = new JButton("Switch Color Scheme"); // button switches between dark and light mode
        invertButton.setPreferredSize(new Dimension(190, 28));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        invertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                invertColors(frame);
                if(myConfig.getMode().equals("dark")) myConfig.setMode("light"); // if user config file says dark mode is enabled, switch to ligh, and vice versa. 
                else myConfig.setMode("dark");
            }
        });
        menuPage.add(invertButton, gbc);

        JButton menuExitButton = new JButton("Exit"); // button switches between dark and light mode
        menuExitButton.setPreferredSize(new Dimension(80, 28));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        menuExitButton.addActionListener(new ActionListener() { // when clicked, exit program
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Confirmation", JOptionPane.YES_NO_OPTION); // confirmation panel for finishing edit user dict
                if (!(confirmation == JOptionPane.YES_OPTION)) {
                    return;
                }
                frame.dispose();
            }
        });
        menuPage.add(menuExitButton, gbc);


        // SAVE SUCCESS SCREEN
        JPanel successPage = new JPanel(new GridBagLayout()); // make the success page
        successPage.setBackground(Color.WHITE);

        JPanel successPanel = new JPanel( new GridBagLayout()); // this container contains the success text and buttons for return/exit 
        gbc.insets = new Insets(20, 0, 0, 0);
        successPanel.setPreferredSize(new Dimension(600, 330));
        successPanel.setBackground(new Color(240,240,240));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        successPage.add(successPanel, gbc);
        
        JLabel successText = new JLabel("Success! Your file has been saved!"); // the success text
        font = successText.getFont();
        successText.setFont(new Font(font.getName(), font.getStyle(), 26)); 
        successText.setBorder(new EmptyBorder(0, 0, 20, 0)); // set bottom margin on text
        successText.setHorizontalAlignment(SwingConstants.CENTER); // center the text
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        successPanel.add(successText, gbc);
        
        JButton successHomeButton = new JButton(); // return to main menu button
        successHomeButton.setText("Main Menu");
        successHomeButton.setPreferredSize(new Dimension(180,50));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTH;
        successPanel.add(successHomeButton, gbc);
        successHomeButton.addActionListener(new ActionListener() { // when clicked, return to main menu
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainCards, "menuScreen");
            }
        });

        JButton successExitButton = new JButton(); // exit program button
        successExitButton.setText("Exit");
        successExitButton.setPreferredSize(new Dimension(180,50));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTH;
        successPanel.add(successExitButton, gbc);
        successExitButton.addActionListener(new ActionListener() { // when clicked, exit program
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });


        mainCards.add("menuScreen", menuPage); // adding every created page to the cards layout
        mainCards.add("spellCheckScreen", spellCheckPage);
        mainCards.add("metricsScreen", metricsPage);
        mainCards.add("userDictScreen", userDictPage);
        mainCards.add("successScreen", successPage);

        frame.add(mainCards, BorderLayout.CENTER); // adding the cards layout to the main frame

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close frame upon exit
        frame.setVisible(true); // making the frame visible  
        frame.setResizable(false); // make frame unresizable
        frame.setLayout(new BorderLayout(0,0));


        invertColors(frame); // double invert colors on load to get rid of unnecessary color artifacts
        invertColors(frame);
        if(myConfig.getMode().equals("dark")) {// if user config file says dark mode is enabled, enable dark mode. 
            invertColors(frame);
        }
    }  

    /**
     * For a given frame, inverts the colors in the frame by calling a 
     * recursive function on the content pane
     *
     * @param frame the frame to invert all the colors in
     */
    private static void invertColors(JFrame frame) { // input is a JFrame, this will invert colors of all components added to the JFrame
        Container contentPane = frame.getContentPane();
        invertColorsRecursive(contentPane);
        frame.repaint();
    }

    /**
     * For a given component, recursively inverts all colors in the component
     * and calls this function on its children (any components added to
     * the parent component) to invert all colors on screen.
     *
     * @param component the component to invert all colors for its children recursively
     */
    private static void invertColorsRecursive(Component component) {
        if (component instanceof Container) {
            Component[] children = ((Container) component).getComponents();
            for (Component child : children) {
                invertColorsRecursive(child);
            }
        }
    
        if (component instanceof JComponent) {
            if (!(component instanceof AbstractButton)) {
                // Invert the background color of the component
                Color currentColor = component.getBackground();
                Color invertedColor = new Color(
                        255 - currentColor.getRed(),
                        255 - currentColor.getGreen(),
                        255 - currentColor.getBlue()
                );
                component.setBackground(invertedColor);
    
                // Invert the foreground color of the component
                Color currentFgColor = component.getForeground();
                Color invertedFgColor = new Color(
                        255 - currentFgColor.getRed(),
                        255 - currentFgColor.getGreen(),
                        255 - currentFgColor.getBlue()
                );
                component.setForeground(invertedFgColor);
            }
        }
    }

    /**
     * Updates the text on the spell checker screen for a new error
     *
     * @param newIndex the index of the next error
     * @param readWords the JTextPane containing the context around the error word
     * @param currentWord the JLabel containing the current error word
     * @param suggest1 the button containing the first correction suggestion
     * @param suggest2 the button containing the second correction suggestion
     * @param suggest3 the button containing the third correction suggestion
     * @param suggest4 the button containing the fourth correction suggestion
     * @param suggest5 the button containing the fifth correction suggestion
     */
    private static void updateTextNextError(int newIndex, JTextPane readWords, JLabel currentWord, JButton suggest1, JButton suggest2, JButton suggest3, JButton suggest4, JButton suggest5) { // input is readWords, currentError, newIndex. updates readWords and currentError to the next error word based on newIndex
        String errorWordText = "Error: ".concat(SpellChecker.stripPunctuation(mySpellChecker.getDocument().getWordsList().get(newIndex))).concat(" (");
        int errorCount = 0;
        for (String error : mySpellChecker.getErrorType()) {
            if(errorCount > 0) errorWordText = errorWordText.concat(", ");
            errorWordText = errorWordText.concat(error);
            errorCount++;
        }
        if(errorWordText.endsWith(", ")) errorWordText = errorWordText.substring(0, errorWordText.length() - 2);
        errorWordText = errorWordText.concat(")");
        currentWord.setText(errorWordText); // set new error word
        
        readWords.setText(""); 
        StyledDocument styledDocument = readWords.getStyledDocument(); // set text block to context surrounding new error word
        try {
            styledDocument.insertString(styledDocument.getLength(), mySpellChecker.getDocument().getContextBefore(newIndex), null); // add context before error word
            SimpleAttributeSet style = new SimpleAttributeSet();
            StyleConstants.setBold(style, true);
            StyleConstants.setForeground(style, Color.RED);
            styledDocument.insertString(styledDocument.getLength(), " " + mySpellChecker.getDocument().getWordsList().get(newIndex) + " ", style); // selected word will be bold and red
            styledDocument.insertString(styledDocument.getLength(), mySpellChecker.getDocument().getContextAfter(newIndex), null); // add context after error word
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        suggest1.setText("N/A"); // reset suggestion buttons text
        suggest2.setText("N/A");
        suggest3.setText("N/A");
        suggest4.setText("N/A");
        suggest5.setText("N/A");

        ArrayList<String> suggestions = mySpellChecker.suggestCorrections(); // set suggestion buttons text
        JButton[] suggestionButtons = {suggest1, suggest2, suggest3, suggest4, suggest5};
        for(int i = 0; i < Math.min(suggestionButtons.length, suggestions.size()); i++) {
            String nextSuggestion = suggestions.get(i);
            suggestionButtons[i].setText(nextSuggestion);
        }
    }

    /**
     * Updates the text on the user dictionary screen for a new word
     *
     * @param userDictWords the JTextPane containing the context around the current dictionary word
     * @param currentWord the JLabel containing the current dictionary word
     */
    private static void updateUserDictText(JTextPane userDictWords, JLabel currentDictWord) {
        userDictWords.setText("");
        StyledDocument styledDocument = userDictWords.getStyledDocument(); // set text block to context surrounding new error word
        try {            
            int startIndex = Math.max(0, userDictIndex-3);
            int endIndex = Math.min(userDictIndex+16, myUserDictionary.userDictToArrayList().size());
            for(int i = startIndex; i < endIndex; i++) {
                SimpleAttributeSet style = new SimpleAttributeSet();
                if(i == userDictIndex) {
                    style = new SimpleAttributeSet();
                    StyleConstants.setBold(style, true);
                    StyleConstants.setForeground(style, Color.RED);
                } 
                styledDocument.insertString(styledDocument.getLength(), myUserDictionary.userDictToArrayList().get(i) + "\n", style); // add words
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        currentDictWord.setText("Word: ".concat(myUserDictionary.userDictToArrayList().get(userDictIndex)));
    }

    /**
     * Shows a popup dialog with help information from help.txt
     *
     * @param frame the frame to show the help dialogue from
     */
    private static void showHelpDialog(JFrame frame) throws IOException { // this function displays the help dialog, stored in the file "help.txt"
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("help.txt"))) { // append each line of help.txt //TODO actually write help.txt with real information
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        
        JDialog dialog = new JDialog(frame, "Help", true); // create the dialog box
        dialog.setSize(900, 800);
        dialog.setLocationRelativeTo(frame);

        JPanel panel = new JPanel(); // create the container that will contain the help pane and close button
        panel.setLayout(new GridBagLayout());

        JTextArea helpMessage = new JTextArea(content.toString()); // add help.txt's text to helpMessage
        helpMessage.setLineWrap(true);
        helpMessage.setWrapStyleWord(true);
        helpMessage.setEditable(false);
        helpMessage.setBackground(new Color (240,240,240));

        JScrollPane scrollPane = new JScrollPane(helpMessage); // make a scrollable pane and add it to the container
        scrollPane.setPreferredSize(new Dimension(800, 700));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;        
        panel.add(scrollPane, gbc);

        JButton closeButton = new JButton("Close"); // make close button and add it to the container
        closeButton.addActionListener(e -> dialog.dispose()); // add "close dialog" functionality upon clicking the close button
        gbc.gridx = 0;
        gbc.gridy = 1;        
        panel.add(closeButton, gbc);

        dialog.add(panel); // add the container to the dialog
        dialog.setVisible(true);
    }

} 
