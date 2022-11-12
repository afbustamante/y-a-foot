package net.andresbustamante.yafoot.commons.util;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LocaleUtilsTest {

    @Test
    void testSupportedLocales() {
        // When
        boolean isFrenchSupported = LocaleUtils.isSupportedLocale(Locale.FRENCH);
        boolean isSpanishSupported = LocaleUtils.isSupportedLocale(new Locale("es"));

        // Then
        assertTrue(isFrenchSupported);
        assertTrue(isSpanishSupported);
    }

    @Test
    void testUnsupportedLocales() {
        // When
        boolean isChineseSupported = LocaleUtils.isSupportedLocale(Locale.CHINESE);

        // Then
        assertFalse(isChineseSupported);
    }
}
