package net.andresbustamante.yafoot.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author andresbustamante
 */
public class MessagesProperties {

    private static final String BASE_NAME = "messages";
    private static final ResourceBundle MESSAGES_EN = ResourceBundle.getBundle(BASE_NAME, new Locale("en"));
    private static final ResourceBundle MESSAGES_ES = ResourceBundle.getBundle(BASE_NAME, new Locale("es"));
    private static final ResourceBundle MESSAGES_FR = ResourceBundle.getBundle(BASE_NAME, new Locale("fr"));

    /**
     * @param key
     * @return
     */
    public static String getValue(String key, Locale locale) {
        if (locale != null) {
            switch (locale.getLanguage()) {
                case "es":
                    return MESSAGES_ES.getString(key);
                case "fr":
                    return MESSAGES_FR.getString(key);
                default:
                    return MESSAGES_EN.getString(key);
            }
        } else {
            return MESSAGES_EN.getString(key);
        }
    }

    /**
     * @param key
     * @return
     */
    public static String getValue(String key, Locale locale, Object... params) {
        String value = getValue(key, locale);

        if (params != null) {
            int i = 0;

            for (Object param : params) {
                String texteARemplacer = "{" + i + "}";

                value = value.replace(texteARemplacer, param.toString());
            }
        }
        return value;
    }
}
