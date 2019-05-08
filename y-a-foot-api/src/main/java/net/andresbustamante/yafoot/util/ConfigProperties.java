package net.andresbustamante.yafoot.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author andresbustamante
 */
public class ConfigProperties {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("config");

    /**
     * @param key
     * @return
     */
    public static String getValue(String key) {
        try {
            return BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return null;
        }
    }
}
