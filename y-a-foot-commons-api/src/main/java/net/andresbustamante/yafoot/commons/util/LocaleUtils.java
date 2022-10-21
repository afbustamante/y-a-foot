package net.andresbustamante.yafoot.commons.util;

import java.util.Arrays;
import java.util.Locale;

public final class LocaleUtils {

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

    private LocaleUtils() {
        // no-op
    }

    public static boolean isSupportedLocale(Locale locale) {
        return Arrays.stream(SUPPORTED_LOCALES).anyMatch(l -> l.equals(locale));
    }
}
