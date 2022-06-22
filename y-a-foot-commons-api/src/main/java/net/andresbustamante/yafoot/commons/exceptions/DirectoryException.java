package net.andresbustamante.yafoot.commons.exceptions;

/**
 * Generic active directory / LDAP Exception for users management
 */
public class DirectoryException extends Exception {

    public DirectoryException(String message) {
        super(message);
    }
}
