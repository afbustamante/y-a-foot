package net.andresbustamante.yafoot.auth.services;

import net.andresbustamante.yafoot.auth.model.enums.RolesEnum;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.LdapException;
import net.andresbustamante.yafoot.commons.model.User;
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
     * @throws LdapException
     */
    void createUser(User user, RolesEnum role, UserContext ctx) throws LdapException;

    /**
     * Updates a user's details in the active directory. It does not updates user's password
     *
     * @param user
     * @param ctx
     * @throws ApplicationException
     * @throws LdapException
     */
    void updateUser(User user, UserContext ctx) throws ApplicationException, LdapException;

    /**
     * Updates a user's password in the LDAP directory
     *
     * @param user
     * @param ctx
     * @throws ApplicationException
     * @throws LdapException
     */
    void updateUserPassword(User user, UserContext ctx) throws ApplicationException, LdapException;

    /**
     * Removes a user from the LDAP directory
     *
     * @param user
     * @param ctx
     * @throws LdapException
     */
    void deleteUser(User user, UserContext ctx) throws LdapException;

    /**
     * Generate a token for a user in order to reset his/her password
     *
     * @param user User to search
     * @return Generated token
     * @throws LdapException
     * @throws ApplicationException If an invalid template is found when sending the notification by email
     */
    String createPasswordResetToken(User user) throws LdapException, ApplicationException;

    /**
     * Updates a user's password in the LDAP directory
     *
     * @param user User to update
     * @param passwordResetToken Token used to validate this update
     * @throws LdapException
     * @throws ApplicationException
     */
    void resetUserPassword(User user, String passwordResetToken) throws LdapException, ApplicationException;
}
