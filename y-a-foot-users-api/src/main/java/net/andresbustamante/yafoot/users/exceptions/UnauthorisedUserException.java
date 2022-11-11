package net.andresbustamante.yafoot.users.exceptions;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;

/**
 * Exception to use for failed authorisation checks.
 */
public class UnauthorisedUserException extends ApplicationException {

    /**
     * Default constructor with log message.
     *
     * @param message Message to log
     */
    public UnauthorisedUserException(String message) {
        super("unauthorised.user.error", message);
    }
}
