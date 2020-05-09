package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.ldap.UserDAO;
import net.andresbustamante.yafoot.model.User;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import net.andresbustamante.yafoot.services.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    private UserDAO userDAO;

    @Autowired
    public UserManagementServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createUser(User user, RolesEnum role, UserContext ctx) throws LdapException {
        userDAO.saveUser(user, role);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUser(User user, UserContext ctx) throws LdapException {
        userDAO.updateUser(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserPassword(User user, UserContext ctx) throws LdapException {
        userDAO.updatePassword(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUser(User user, UserContext ctx) throws LdapException {
        userDAO.deleteUser(user);
    }
}
