package net.andresbustamante.yafoot.users.repository;

import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.model.enums.RolesEnum;

/**
 * Interface describing the operations allowed on user in the LDAP directory.
 */
public interface UserRepository {

    /**
     * Creates a user in LDAP directory.
     *
     * @param usr User to create
     * @param role Role to give to the user when created
     */
    void saveUser(User usr, RolesEnum role);

    /**
     * Deletes a user from LDAP directory.
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
