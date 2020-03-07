package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.InvalidCredentialsException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.model.User;

/**
 * Authentication service
 */
public interface UserAuthenticationService {

    /**
     * Authenticate a user and return a JWT token
     *
     * @param user User to authenticate
     * @return Authentication token (JWT)
     */
    String authenticate(User user) throws InvalidCredentialsException;

    /**
     * Load a user for authentication by using his email address
     *
     * @param email Email address to search
     * @return User using this email address for authentication
     * @throws LdapException
     */
    User findUserByEmail(String email) throws LdapException;
}
