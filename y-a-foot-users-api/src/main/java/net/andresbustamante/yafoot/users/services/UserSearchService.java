package net.andresbustamante.yafoot.users.services;

import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;

public interface UserSearchService {

    /**
     * Load a user for authentication by using his email address
     *
     * @param email Email address to search
     * @return User using this email address for authentication
     * @throws DirectoryException
     */
    User findUserByEmail(String email) throws DirectoryException;

    /**
     * Find a user by the token created for reset his authentication password
     *
     * @param token Token to search
     * @return User found for this token
     */
    User findUserByToken(String token) throws DirectoryException;
}
