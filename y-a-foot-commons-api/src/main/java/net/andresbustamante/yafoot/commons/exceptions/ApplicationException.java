package net.andresbustamante.yafoot.commons.exceptions;

import lombok.Getter;

/**
 * Generic functional exception
 */
public class ApplicationException extends Exception {

    @Getter
    protected final String code;

    public ApplicationException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        this.code = null;
    }
}
