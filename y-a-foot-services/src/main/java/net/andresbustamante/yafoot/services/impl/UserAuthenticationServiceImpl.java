package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.exceptions.InvalidCredentialsException;
import net.andresbustamante.yafoot.ldap.UserDAO;
import net.andresbustamante.yafoot.model.User;
import net.andresbustamante.yafoot.services.UserAuthenticationService;
import net.andresbustamante.yafoot.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private JwtTokenUtils jwtTokenUtils;

    private UserDAO userDAO;

    @Autowired
    public UserAuthenticationServiceImpl(JwtTokenUtils jwtTokenUtils, UserDAO userDAO) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.userDAO = userDAO;
    }

    @Transactional
    @Override
    public User authenticate(User user) throws InvalidCredentialsException {
        User authenticatedUser = userDAO.authenticateUser(user.getEmail(), user.getPassword());

        if (authenticatedUser != null) {
            authenticatedUser.setToken(jwtTokenUtils.generateToken(user.getEmail()));
            return authenticatedUser;
        } else {
            throw new InvalidCredentialsException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserByEmail(String email) {
        return userDAO.findUserAuthDetailsByUid(email);
    }
}
