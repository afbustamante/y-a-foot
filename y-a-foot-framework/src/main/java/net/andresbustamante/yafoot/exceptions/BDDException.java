package net.andresbustamante.yafoot.exceptions;

import net.andresbustamante.framework.exceptions.DatabaseException;

/**
 * @author andresbustamante
 */
public class BDDException extends DatabaseException {

    public BDDException() {
    }

    public BDDException(String message) {
        super(message);
    }
}
