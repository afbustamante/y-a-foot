package net.andresbustamante.yafoot.auth.services.impl;

import net.andresbustamante.yafoot.users.repository.UserRepository;
import net.andresbustamante.yafoot.users.services.UserSearchService;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
public class UserSearchServiceImpl implements UserSearchService {

    private final UserRepository userRepository;

    @Autowired
    public UserSearchServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true, propagation = REQUIRES_NEW)
    public User findUserByEmail(String email) throws DirectoryException {
        return userRepository.findUserByEmail(email);
    }

    @Override
    @Transactional(readOnly = true, propagation = REQUIRES_NEW)
    public User findUserByToken(String token) {
        return userRepository.findUserByToken(token);
    }
}