package net.andresbustamante.yafoot.commons.exceptions;

/**
 * Database exception when executing database operations
 *
 * @author andresbustamante
 */
public class DatabaseException extends Exception {

    public DatabaseException(String message) {
        super(message);
    }
}
