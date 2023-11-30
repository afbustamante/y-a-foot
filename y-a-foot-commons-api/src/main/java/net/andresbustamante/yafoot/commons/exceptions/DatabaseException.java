package net.andresbustamante.yafoot.commons.exceptions;

/**
 * Database exception when executing database operations.
 *
 * @author andresbustamante
 */
public class DatabaseException extends Exception {

    /**
     * Default constructor with a log message.
     *
     * @param message Message to log
     */
    public DatabaseException(final String message) {
        super(message);
    }
}
