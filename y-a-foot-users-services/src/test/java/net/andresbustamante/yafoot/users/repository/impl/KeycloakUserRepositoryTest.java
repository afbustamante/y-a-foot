package net.andresbustamante.yafoot.users.repository.impl;

import net.andresbustamante.yafoot.users.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KeycloakUserRepositoryTest {

    @InjectMocks
    private KeycloakUserRepositoryImpl keycloakUserRepository;

    @Test
    void findUserByEmail() {
        // When
        var result = keycloakUserRepository.findUserByEmail("test@email.com");

        // Then
        assertNull(result);
    }

    @Test
    void deleteUser() {
        // Given
        User user = new User();

        // When-Then
        assertDoesNotThrow(() -> keycloakUserRepository.deleteUser(user));
    }
}
