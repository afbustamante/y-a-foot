package net.andresbustamante.yafoot.commons.exceptions;

import lombok.Getter;

/**
 * Generic functional exception.
 */
public class ApplicationException extends Exception {

    /**
     * Message code used for translation.
     */
    @Getter
    private final String code;

    /**
     * Default constructor with a message code and a log message.
     *
     * @param code Message code to use for translation
     * @param message Message to log
     */
    public ApplicationException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Default constructor with a log message and the cause of the exception.
     *
     * @param message Message to log
     * @param cause Cause of the exception
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        this.code = null;
    }
}
