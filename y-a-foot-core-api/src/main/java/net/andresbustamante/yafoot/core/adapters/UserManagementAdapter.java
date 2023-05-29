package net.andresbustamante.yafoot.core.adapters;

import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.commons.model.UserContext;

/**
 * Adapter to implement for user storage operations.
 */
public interface UserManagementAdapter {

    /**
     * Updates an existing user.
     *
     * @param user User to update
     * @param context User context
     * @throws DirectoryException When a problem comes from the storage layer
     */
    void updateUser(User user, UserContext context) throws DirectoryException;

    /**
     * Deletes an existing user.
     *
     * @param user User to delete.
     * @param context User context
     * @throws DirectoryException When a problem comes from the storage layer
     */
    void deleteUser(User user, UserContext context) throws DirectoryException;
}
