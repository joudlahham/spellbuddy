import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTestConfiguration {

    private Configuration configuration;

    // Initialize a new Configuration instance before each test
    @BeforeEach
    public void setUp() {
        configuration = new Configuration();
    }

    // Test 1: Set the mode to 'light' and check that the mode is 'light'
    @Test
    public void testSetLightMode() {
        configuration.setMode("light");
        assertEquals("light", configuration.getMode());
    }

    // Test 2: Set the mode to 'dark' and check that the mode is 'dark'
    @Test
    public void testSetAndGetMode() {
        configuration.setMode("dark");
        assertEquals("dark", configuration.getMode());
    }

    // Test 3: Check that the mode is 'dark' in the new instance (should be loaded from file)
    @Test
    public void testSaveAndLoadConfiguration() {
        configuration.setMode("dark");
        Configuration newConfiguration = new Configuration();
        assertEquals("dark", newConfiguration.getMode());
    }
}