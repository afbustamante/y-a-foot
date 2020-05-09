package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.model.User;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.enums.RolesEnum;

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
}
