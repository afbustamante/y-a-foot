package net.andresbustamante.yafoot.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigPropertiesTest {

    @Test
    public void getValueCleExistante() {
        String valeur = ConfigProperties.getValue("test.property");
        assertNotNull(valeur);
        assertEquals("Test value", valeur);
    }

    @Test
    public void getValueCleInexistante() {
        String valeur = ConfigProperties.getValue("another.property");
        assertNull(valeur);
    }
}