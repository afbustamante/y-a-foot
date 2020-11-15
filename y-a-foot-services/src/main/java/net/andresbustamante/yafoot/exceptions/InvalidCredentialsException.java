package net.andresbustamante.yafoot.exceptions;

/**
 * Authentication exception
 */
public class InvalidCredentialsException extends ApplicationException {

    public InvalidCredentialsException(String message) {
        super("invalid.credentials.error", message);
    }
}
