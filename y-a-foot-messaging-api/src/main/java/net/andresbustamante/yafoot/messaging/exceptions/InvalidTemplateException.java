package net.andresbustamante.yafoot.messaging.exceptions;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;

/**
 * When an email template is not valid for sending.
 */
public class InvalidTemplateException extends ApplicationException {

    /**
     * Default constructor with a message to log and the cause of the exception.
     *
     * @param message Message to log
     * @param cause Cause of the exception
     */
    public InvalidTemplateException(String message, Throwable cause) {
        super("invalid.template.error", message);
        this.initCause(cause);
    }
}
