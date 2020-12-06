package net.andresbustamante.yafoot.exceptions;

public class PlayerNotFoundException extends ApplicationException {

    public PlayerNotFoundException(String message) {
        super("player.not.found.error", message);
    }
}