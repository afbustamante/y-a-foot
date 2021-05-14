package net.andresbustamante.yafoot.auth.services;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.LdapException;
import net.andresbustamante.yafoot.commons.model.User;

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
    User authenticate(User user) throws LdapException, ApplicationException;

    /**
     * Load a user for authentication by using his email address
     *
     * @param email Email address to search
     * @return User using this email address for authentication
     * @throws LdapException
     */
    User findUserByEmail(String email) throws LdapException;

    /**
     * Find a user by the token created for reset his authentication password
     *
     * @param token Token to search
     * @return User found for this token
     */
    User findUserByToken(String token) throws LdapException;
}
