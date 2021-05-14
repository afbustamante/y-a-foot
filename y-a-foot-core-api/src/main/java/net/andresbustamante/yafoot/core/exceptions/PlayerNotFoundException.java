package net.andresbustamante.yafoot.core.exceptions;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;

public class PlayerNotFoundException extends ApplicationException {

    public PlayerNotFoundException(String message) {
        super("player.not.found.error", message);
    }
}
