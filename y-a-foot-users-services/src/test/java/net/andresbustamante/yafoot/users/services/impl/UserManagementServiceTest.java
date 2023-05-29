package net.andresbustamante.yafoot.users.services.impl;

import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.commons.services.AbstractServiceUnitTest;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserManagementServiceImpl}.
 */
class UserManagementServiceTest extends AbstractServiceUnitTest {

    @InjectMocks
    private UserManagementServiceImpl userManagementService;

    @Mock
    private UserRepository userRepository;

    @Test
    void updateUser() throws Exception {
        // Given
        User user = new User("test@email.com");

        // When
        assertDoesNotThrow(() -> userManagementService.updateUser(user, new UserContext()));

        // Then
        verify(userRepository).updateUser(any(User.class));
    }

    @Test
    void deleteUser() throws Exception {
        // Given
        User user = new User("test@email.com");

        // When
        assertDoesNotThrow(() -> userManagementService.deleteUser(user, new UserContext()));

        // Then
        verify(userRepository).deleteUser(any(User.class));
    }
}
