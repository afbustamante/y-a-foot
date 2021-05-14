package net.andresbustamante.yafoot.core.exceptions;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;

/**
 * Application exception for management actions over past matches
 */
public class PastMatchException extends ApplicationException {

    public PastMatchException(String message) {
        super("match.past.date.management.error", message);
    }
}
