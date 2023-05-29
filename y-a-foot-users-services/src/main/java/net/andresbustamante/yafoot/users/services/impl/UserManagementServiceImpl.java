package net.andresbustamante.yafoot.users.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.repository.UserRepository;
import net.andresbustamante.yafoot.users.services.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final Logger log = LoggerFactory.getLogger(UserManagementServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserManagementServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateUser(User user, UserContext ctx) throws DirectoryException {
        userRepository.updateUser(user);
        log.info("Details successfully updated for user {}", user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void deleteUser(User user, UserContext ctx) throws DirectoryException {
        userRepository.deleteUser(user);
        log.info("User {} successfully deleted", user.getEmail());
    }
}
