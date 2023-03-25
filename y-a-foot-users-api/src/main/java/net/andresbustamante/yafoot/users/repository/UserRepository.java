package net.andresbustamante.yafoot.users.repository;

import net.andresbustamante.yafoot.users.model.User;

/**
 * Interface describing the operations allowed on user in the LDAP directory.
 */
public interface UserRepository {

    /**
     * Deletes a user from active directory.
     *
     * @param usr User to delete
     */
    void deleteUser(User usr);

    /**
     * Find a user by using his email address.
     *
     * @param email Email address to search
     * @return User found using this email. Null if no user is found.
     */
    User findUserByEmail(String email);
}
