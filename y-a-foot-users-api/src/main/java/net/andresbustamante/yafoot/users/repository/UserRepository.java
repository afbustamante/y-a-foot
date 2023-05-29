package net.andresbustamante.yafoot.users.repository;

import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;

/**
 * Interface describing the operations allowed on user in the LDAP directory.
 */
public interface UserRepository {

    /**
     * Update user's personal details in the active directory.
     *
     * @param usr User with updated details
     */
    void updateUser(User usr) throws DirectoryException;

    /**
     * Deletes a user from active directory.
     *
     * @param usr User to delete
     */
    void deleteUser(User usr) throws DirectoryException;

    /**
     * Find a user by using his email address.
     *
     * @param email Email address to search
     * @return User found using this email. Null if no user is found.
     */
    User findUserByEmail(String email) throws DirectoryException;
}
