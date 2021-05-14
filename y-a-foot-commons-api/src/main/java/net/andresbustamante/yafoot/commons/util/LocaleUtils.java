package net.andresbustamante.yafoot.commons.util;

import java.util.Locale;

public class LocaleUtils {

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    static final String SPANISH = "es";
    static final String FRENCH = "fr";
    static final String ENGLISH = "en";

    private static final Locale[] SUPPORTED_LOCALES = {
            Locale.ENGLISH,
            Locale.UK,
            Locale.FRENCH,
            Locale.FRANCE,
            new Locale("es")
    };

    private LocaleUtils() {}

    public static boolean isSupportedLocale(Locale locale) {
        for (Locale l : SUPPORTED_LOCALES) {
            if (l.equals(locale)) {
                return true;
            }
        }
        return false;
    }
}
