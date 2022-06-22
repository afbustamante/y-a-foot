package net.andresbustamante.yafoot.auth.services.impl;

import net.andresbustamante.yafoot.users.exceptions.InvalidCredentialsException;
import net.andresbustamante.yafoot.users.repository.UserRepository;
import net.andresbustamante.yafoot.users.services.UserAuthenticationService;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.commons.util.JwtTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;

    private final Logger log = LoggerFactory.getLogger(UserAuthenticationServiceImpl.class);

    @Autowired
    public UserAuthenticationServiceImpl(JwtTokenUtils jwtTokenUtils, UserRepository userRepository) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User authenticate(User user) throws ApplicationException {
        User authenticatedUser = userRepository.authenticateUser(user.getEmail(), user.getPassword());

        if (authenticatedUser != null) {
            authenticatedUser.setToken(jwtTokenUtils.generateToken(user.getEmail()));
            log.info("User {} successfully authenticated", user.getEmail());
            return authenticatedUser;
        } else {
            log.warn("Invalid credentials used when trying to authenticate user");
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

}
