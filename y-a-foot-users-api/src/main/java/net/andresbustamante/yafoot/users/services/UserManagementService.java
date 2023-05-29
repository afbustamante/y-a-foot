package net.andresbustamante.yafoot.users.services;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.commons.model.UserContext;

/**
 * User directory management service.
 */
public interface UserManagementService {

    /**
     * Updates a user's details in the active directory. It does not updates user's password.
     *
     * @param user User with updated details
     * @param ctx
     * @throws ApplicationException
     * @throws DirectoryException
     */
    void updateUser(User user, UserContext ctx) throws ApplicationException, DirectoryException;

    /**
     * Removes a user from the internal user directory.
     *
     * @param user
     * @param ctx
     * @throws DirectoryException
     */
    void deleteUser(User user, UserContext ctx) throws DirectoryException;

}
