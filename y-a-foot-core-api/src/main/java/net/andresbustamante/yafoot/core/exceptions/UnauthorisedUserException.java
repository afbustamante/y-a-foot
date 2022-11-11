package net.andresbustamante.yafoot.core.exceptions;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;

public class UnauthorisedUserException extends ApplicationException {

    /**
     * Default constructor with a message to log.
     *
     * @param message Message to log
     */
    public UnauthorisedUserException(String message) {
        super("unauthorised.user.error", message);
    }
}
