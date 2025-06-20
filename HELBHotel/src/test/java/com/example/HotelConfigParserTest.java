package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HotelConfigParserTest {

    @Test
    void testConfigParserDoesNotCrash() {
        try {
            String path = "src/main/java/com/example/.hconfig";
            String[][][] config = HotelConfigParser.loadHotelConfig(path);
            assertNotNull(config, "The configuration should not be null.");
        } catch (Exception e) {
            fail("Parsing the .hconfig file should not throw an exception: " + e.getMessage());
        }
    }
}
