package net.andresbustamante.yafoot.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigPropertiesTest {

    @Test
    void getValueCleExistante() {
        String valeur = ConfigProperties.getValue("test.property");
        assertNotNull(valeur);
        assertEquals("Test value", valeur);
    }

    @Test
    void getValueCleInexistante() {
        String valeur = ConfigProperties.getValue("another.property");
        assertNull(valeur);
    }
}