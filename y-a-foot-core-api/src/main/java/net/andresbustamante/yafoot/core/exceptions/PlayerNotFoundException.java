package net.andresbustamante.yafoot.core.exceptions;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;

public class PlayerNotFoundException extends ApplicationException {

    /**
     * Default constructor with a message to log.
     *
     * @param message Message to log
     */
    public PlayerNotFoundException(String message) {
        super("player.not.found.error", message);
    }
}
