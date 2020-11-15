package net.andresbustamante.yafoot.exceptions;

public class InvalidTemplateException extends ApplicationException {

    public InvalidTemplateException(String message, Throwable cause) {
        super("invalid.template.error", message);
        this.initCause(cause);
    }
}
