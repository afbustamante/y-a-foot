package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.exceptions.InvalidCredentialsException;
import net.andresbustamante.yafoot.ldap.UserRepository;
import net.andresbustamante.yafoot.model.User;
import net.andresbustamante.yafoot.services.UserAuthenticationService;
import net.andresbustamante.yafoot.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private JwtTokenUtils jwtTokenUtils;

    private UserRepository userRepository;

    @Autowired
    public UserAuthenticationServiceImpl(JwtTokenUtils jwtTokenUtils, UserRepository userRepository) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User authenticate(User user) throws InvalidCredentialsException {
        User authenticatedUser = userRepository.authenticateUser(user.getEmail(), user.getPassword());

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
        return userRepository.findUserByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserByToken(String token) {
        return userRepository.findUserByToken(token);
    }
}
