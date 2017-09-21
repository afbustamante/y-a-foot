package net.andresbustamante.yafoot.util;

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
        return BUNDLE.getString(key);
    }
}
