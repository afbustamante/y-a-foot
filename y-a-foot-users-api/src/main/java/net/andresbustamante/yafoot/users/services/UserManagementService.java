package net.andresbustamante.yafoot.users.services;

import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.commons.model.UserContext;

/**
 * User directory management service.
 */
public interface UserManagementService {

    /**
     * Removes a user from the internal user directory.
     *
     * @param user
     * @param ctx
     * @throws DirectoryException
     */
    void deleteUser(User user, UserContext ctx) throws DirectoryException;

}
