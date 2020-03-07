package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.exceptions.InvalidCredentialsException;
import net.andresbustamante.yafoot.ldap.UserDAO;
import net.andresbustamante.yafoot.model.User;
import net.andresbustamante.yafoot.services.UserAuthenticationService;
import net.andresbustamante.yafoot.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private JwtTokenUtils jwtTokenUtils;

    private UserDAO userDAO;

    @Autowired
    public UserAuthenticationServiceImpl(JwtTokenUtils jwtTokenUtils, UserDAO userDAO) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.userDAO = userDAO;
    }

    @Override
    public String authenticate(User user) throws InvalidCredentialsException {
        boolean authenticated = userDAO.authenticateUser(user.getEmail(), user.getPassword());

        if (authenticated) {
            return jwtTokenUtils.generateToken(user.getEmail());
        } else {
            throw new InvalidCredentialsException();
        }
    }

    @Override
    public User findUserByEmail(String email) {
        return userDAO.findUserAuthDetailsByUid(email);
    }
}
