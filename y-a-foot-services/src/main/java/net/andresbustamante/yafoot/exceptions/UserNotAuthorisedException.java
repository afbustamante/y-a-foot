package net.andresbustamante.yafoot.exceptions;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;

public class UserNotAuthorisedException extends ApplicationException {

    public UserNotAuthorisedException(String message) {
        super("unauthorised.user.error", message);
    }
}
