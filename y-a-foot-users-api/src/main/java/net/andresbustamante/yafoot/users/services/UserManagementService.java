package net.andresbustamante.yafoot.users.services;

import net.andresbustamante.yafoot.commons.model.enums.RolesEnum;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.commons.model.UserContext;

/**
 * User directory management service
 */
public interface UserManagementService {

    /**
     * Creates a new user with the given role in the LDAP directory
     *
     * @param user
     * @param role
     * @param ctx
     * @throws DirectoryException
     */
    void createUser(User user, RolesEnum role, UserContext ctx) throws DirectoryException;

    /**
     * Updates a user's details in the active directory. It does not updates user's password
     *
     * @param user
     * @param ctx
     * @throws ApplicationException
     * @throws DirectoryException
     */
    void updateUser(User user, UserContext ctx) throws ApplicationException, DirectoryException;

    /**
     * Updates a user's password in the LDAP directory
     *
     * @param user
     * @param ctx
     * @throws ApplicationException
     * @throws DirectoryException
     */
    void updateUserPassword(User user, UserContext ctx) throws ApplicationException, DirectoryException;

    /**
     * Removes a user from the LDAP directory
     *
     * @param user
     * @param ctx
     * @throws DirectoryException
     */
    void deleteUser(User user, UserContext ctx) throws DirectoryException;

    /**
     * Generate a token for a user in order to reset his/her password
     *
     * @param user User to search
     * @return Generated token
     * @throws DirectoryException
     * @throws ApplicationException If an invalid template is found when sending the notification by email
     */
    String createPasswordResetToken(User user) throws DirectoryException, ApplicationException;

    /**
     * Updates a user's password in the LDAP directory
     *
     * @param user User to update
     * @param passwordResetToken Token used to validate this update
     * @throws DirectoryException
     * @throws ApplicationException
     */
    void resetUserPassword(User user, String passwordResetToken) throws DirectoryException, ApplicationException;
}
