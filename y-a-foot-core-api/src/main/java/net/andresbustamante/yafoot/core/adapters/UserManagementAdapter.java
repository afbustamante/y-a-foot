package net.andresbustamante.yafoot.core.adapters;

import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.commons.model.UserContext;

public interface UserManagementAdapter {

    void createUser(User user, UserContext context) throws DirectoryException;

    void updateUser(User user, UserContext context) throws DirectoryException;

    void deleteUser(User user, UserContext context) throws DirectoryException;

    User findUserByEmail(String email) throws DirectoryException;
}
