import java.io.*;
import java.util.Properties;

/**
 * The Configuration class represents saved configurations and settings 
 * expected to carry through to the user's next use of the spell checker.
 * 
 * @author      Joud Adel Al-Lahham <jallahha@uwo.ca>
 * @author      Jodi Keizer <jkeizer@uwo.ca>
 * @author      Anna Ma <ama92@uwo.ca>
 * @author      Ivan Quan <iquan5@uwo.ca>
 * @author      Kevin Xie <kxie49@uwo.ca>
 * @version     1.0
 */

public class Configuration {
    private static final String CONFIGURATION_FILE = "configuration.properties";
    private Properties properties;

    /**
     * Constructor for the configuration class. Makes a property object
     * and loads the properties object with the configuration as
     * specified in the file
     */
    public Configuration() {
        this.properties = new Properties();
        loadConfiguration();
    }

    /**
     * Loads the configuration of the document object from the file named
     * "configuration.properties". 
     */
    private void loadConfiguration() {
        try (InputStream input = new FileInputStream(CONFIGURATION_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            // Handles file not found or other exceptions
            e.printStackTrace();
        }
    }

    /**
     * Saves the current configuration to a file to keep configuration 
     * consistent across multiple sessions of the program.
     */
    public void saveConfiguration() {
        try (OutputStream output = new FileOutputStream(CONFIGURATION_FILE)) {
            properties.store(output, null);
        } catch (IOException e) {
            // Handles exceptions
            e.printStackTrace();
        }
    }

    /**
     * Returns  the current interface color mode (light mode or dark mode) 
     * 
     * @return String containing the interface color mode
     */
    public String getMode() {
        return properties.getProperty("mode", "light"); // Light mode by default
    }

    /**
     * Sets the current interface color scheme to be dark or light mode
     * based on the argument provided
     *
     * @param mode dark or light mode
     */
    public void setMode(String mode) {
        properties.setProperty("mode", mode);
        saveConfiguration(); // Save updated configuration to file
    }
}