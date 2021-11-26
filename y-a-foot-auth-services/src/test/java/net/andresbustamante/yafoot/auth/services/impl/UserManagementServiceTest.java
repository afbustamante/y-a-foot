package net.andresbustamante.yafoot.auth.services.impl;

import net.andresbustamante.yafoot.auth.model.enums.RolesEnum;
import net.andresbustamante.yafoot.auth.repository.UserRepository;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.model.User;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.commons.services.AbstractServiceTest;
import net.andresbustamante.yafoot.messaging.services.MessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserManagementServiceTest extends AbstractServiceTest {

    @InjectMocks
    private UserManagementServiceImpl userManagementService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessagingService messagingService;

    @BeforeEach
    public void setUp() throws Exception {
        String passwordResetUrl = "http://dummy-url/{0}";
        ReflectionTestUtils.setField(userManagementService, "passwordResetUrl", passwordResetUrl);
    }

    @Test
    void createUser() throws Exception {
        // Given
        User user = new User("test@email.com");
        user.setPassword("password");

        // When
        userManagementService.createUser(user, RolesEnum.PLAYER, new UserContext());

        // Then
        verify(userRepository).saveUser(any(User.class), any(RolesEnum.class));
    }

    @Test
    void updateUser() throws Exception {
        // Given
        User user = new User("test@email.com");

        // When
        userManagementService.updateUser(user, new UserContext());

        // Then
        verify(userRepository).updateUser(any(User.class));
    }

    @Test
    void updateUserPassword() throws Exception {
        // Given
        User user = new User("test@email.com");
        user.setPassword("password");

        // When
        userManagementService.updateUserPassword(user, new UserContext("test@email.com"));

        // Then
        verify(userRepository).updatePassword(any(User.class));
    }

    @Test
    void deleteUser() throws Exception {
        // Given
        User user = new User("test@email.com");

        // When
        userManagementService.deleteUser(user, new UserContext());

        // Then
        verify(userRepository).deleteUser(any(User.class));
    }

    @Test
    void createPasswordResetToken() throws Exception {
        // Given
        User user = new User("test@email.com");

        // When
        String token = userManagementService.createPasswordResetToken(user);

        // Then
        assertNotNull(token);
        assertTrue(token.matches("^[A-F0-9]{16}$"));
        verify(userRepository).saveTokenForUser(anyString(), any(User.class));
        verify(userRepository).findUserByToken(anyString());
    }

    @Test
    void resetUserPassword() throws Exception {
        // Given
        User user = new User("test@email.com");
        user.setPassword("password");
        String token = "token";
        User tokenUser = new User("test@email.com");

        // When
        when(userRepository.findUserByToken(anyString())).thenReturn(tokenUser);
        userManagementService.resetUserPassword(user, token);

        // Then
        verify(userRepository).updatePassword(any(User.class));
        verify(userRepository).removeTokenForUser(any(User.class));
    }

    @Test
    void resetUserPasswordUnauthorisedUser() throws Exception {
        // Given
        User user = new User("anotheruser@email.com");
        user.setPassword("password");
        String token = userManagementService.createPasswordResetToken(user);
        User tokenUser = new User("test@email.com");

        // When
        when(userRepository.findUserByToken(anyString())).thenReturn(tokenUser);
        assertThrows(ApplicationException.class, () -> userManagementService.resetUserPassword(user, token));

        // Then
        verify(userRepository, never()).updatePassword(any(User.class));
        verify(userRepository, never()).removeTokenForUser(any(User.class));
    }
}