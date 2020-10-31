package net.andresbustamante.yafoot.exceptions;

/**
 * Database exception when executing database operations
 *
 * @author andresbustamante
 */
public class DatabaseException extends Exception {

    public DatabaseException() {
    }

    public DatabaseException(String message) {
        super(message);
    }
}
