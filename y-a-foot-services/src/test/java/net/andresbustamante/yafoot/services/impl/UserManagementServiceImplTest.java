package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.ldap.UserRepository;
import net.andresbustamante.yafoot.model.User;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import net.andresbustamante.yafoot.services.MessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

class UserManagementServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private UserManagementServiceImpl userManagementService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessagingService messagingService;

    @BeforeEach
    public void setUp() throws Exception {
        String passwordResetUrl = "http://dummy-url/{0}";
        MockitoAnnotations.initMocks(this);
        FieldSetter.setField(userManagementService, userManagementService.getClass().getDeclaredField("passwordResetUrl"),
                passwordResetUrl);
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
        userManagementService.updateUserPassword(user, new UserContext());

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
}