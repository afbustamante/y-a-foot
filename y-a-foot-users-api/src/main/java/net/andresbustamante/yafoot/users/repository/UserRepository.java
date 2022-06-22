package net.andresbustamante.yafoot.users.repository;

import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.commons.model.enums.RolesEnum;

/**
 * Interface describing the operations allowed on user in the LDAP directory
 */
public interface UserRepository {

    /**
     * Creates a user in LDAP directory
     *
     * @param usr User to create
     * @param role Role to give to the user when created
     */
    void saveUser(User usr, RolesEnum role);

    /**
     * Update user's personal details in LDAP directory
     *
     * @param usr
     */
    void updateUser(User usr);

    /**
     * Update a user's password
     *
     * @param usr User to update
     */
    void updatePassword(User usr);

    /**
     * Deletes a user from LDAP directory
     *
     * @param usr User to delete
     */
    void deleteUser(User usr);

    /**
     * Find a user by using his email address
     *
     * @param email Email address to search
     * @return
     */
    User findUserByEmail(String email);

    /**
     * Authenticate user by using his email address and his password
     *
     * @param uid User's ID (email address)
     * @param password User's password
     * @return Returns user's details only if the authentication information is valid
     */
    User authenticateUser(String uid, String password);

    /**
     * Find a user by the token generated to reset his password
     *
     * @param token Token to search
     * @return The user found for this token or null if nobody is found
     */
    User findUserByToken(String token);

    /**
     * Sets a token for password change to the user
     *
     * @param token Token to set
     * @param user User to modify
     */
    void saveTokenForUser(String token, User user);

    /**
     * Removes the token for password change given to a user
     *
     * @param user User to update
     */
    void removeTokenForUser(User user);
}
