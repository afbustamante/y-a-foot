package net.andresbustamante.yafoot.commons.exceptions;

/**
 * Generic active directory / LDAP Exception for users management.
 */
public class DirectoryException extends Exception {

    /**
     * Default constructor with a log message.
     *
     * @param message Message to log
     */
    public DirectoryException(final String message) {
        super(message);
    }
}
