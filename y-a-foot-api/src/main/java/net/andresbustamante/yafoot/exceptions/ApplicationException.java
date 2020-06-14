package net.andresbustamante.yafoot.exceptions;

public class ApplicationException extends Exception {

    private final String code;

    public ApplicationException() {
        this.code = null;
    }

    public ApplicationException(String message) {
        super(message);
        this.code = null;
    }

    public ApplicationException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        this.code = null;
    }

    public String getCode() {
        return code;
    }
}
