package net.andresbustamante.yafoot.users.services;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;

/**
 * Authentication service
 */
public interface UserAuthenticationService {

    /**
     * Authenticate a user and return a JWT token
     *
     * @param user User to authenticate
     * @return Authenticated user including a JWT token
     */
    User authenticate(User user) throws DirectoryException, ApplicationException;

}
