package net.andresbustamante.yafoot.exceptions;

public class UserNotAuthorisedException extends ApplicationException {

    public UserNotAuthorisedException(String message) {
        super("unauthorised.user.error", message);
    }
}
