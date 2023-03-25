package net.andresbustamante.yafoot.users.repository.impl;

import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class KeycloakUserRepositoryImpl implements UserRepository {

    @Override
    public void deleteUser(User usr) {
        // TODO Implement this method
    }

    @Override
    public User findUserByEmail(String email) {
        // TODO Implement this method
        return null;
    }
}
