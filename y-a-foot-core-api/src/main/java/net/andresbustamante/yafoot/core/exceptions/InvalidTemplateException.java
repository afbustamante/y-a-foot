package net.andresbustamante.yafoot.core.exceptions;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;

public class InvalidTemplateException extends ApplicationException {

    public InvalidTemplateException(String message, Throwable cause) {
        super("invalid.template.error", message);
        this.initCause(cause);
    }
}
