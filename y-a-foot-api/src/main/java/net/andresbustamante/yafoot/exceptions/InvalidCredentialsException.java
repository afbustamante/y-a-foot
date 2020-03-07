package net.andresbustamante.yafoot.exceptions;

/**
 * Authentication exception
 */
public class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException() {
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
