package net.andresbustamante.yafoot.auth.exceptions;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;

/**
 * Authentication exception
 */
public class InvalidCredentialsException extends ApplicationException {

    public InvalidCredentialsException(String message) {
        super("invalid.credentials.error", message);
    }
}
