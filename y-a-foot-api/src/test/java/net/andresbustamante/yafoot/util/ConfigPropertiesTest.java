package net.andresbustamante.yafoot.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigPropertiesTest {

    @Test
    void getValueExistingKey() {
        String value = ConfigProperties.getValue("test.property");
        assertNotNull(value);
        assertEquals("Test value", value);
    }

    @Test
    void getValueUnknownKey() {
        String value = ConfigProperties.getValue("another.property");
        assertNull(value);
    }
}