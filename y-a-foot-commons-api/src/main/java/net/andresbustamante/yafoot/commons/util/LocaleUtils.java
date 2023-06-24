package net.andresbustamante.yafoot.commons.util;

import java.util.Locale;
import java.util.Set;

/**
 * Locale and i18n utilities.
 */
public final class LocaleUtils {

    /**
     * Default system locale.
     */
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    /**
     * List of supported locales.
     */
    private static final Locale[] SUPPORTED_LOCALES = {
            Locale.ENGLISH,
            Locale.UK,
            Locale.FRENCH,
            Locale.FRANCE,
            new Locale("es")
    };

    private LocaleUtils() {
        // no-op
    }

    /**
     * Indicates whether a locale is already supported.
     *
     * @param locale Locale to check
     * @return True if a locale is supported
     */
    public static boolean isSupportedLocale(Locale locale) {
        return Set.of(SUPPORTED_LOCALES).contains(locale);
    }
}
