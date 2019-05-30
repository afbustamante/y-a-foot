package net.andresbustamante.yafoot.exceptions;

/**
 * Exception dédié aux opérations d'accès aux données de l'application
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
