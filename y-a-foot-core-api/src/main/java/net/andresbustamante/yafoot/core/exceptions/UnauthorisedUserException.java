package net.andresbustamante.yafoot.core.exceptions;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;

public class UnauthorisedUserException extends ApplicationException {

    public UnauthorisedUserException(String message) {
        super("unauthorised.user.error", message);
    }
}
