package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.ldap.UserDAO;
import net.andresbustamante.yafoot.model.User;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

class UserManagementServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private UserManagementServiceImpl userManagementService;

    @Mock
    private UserDAO userDAO;

    @Test
    void createUser() throws Exception {
        // Given
        User user = new User("test@email.com");
        user.setPassword("password");

        // When
        userManagementService.createUser(user, RolesEnum.PLAYER, new UserContext());

        // Then
        verify(userDAO).saveUser(any(User.class), any(RolesEnum.class));
    }

    @Test
    void updateUser() throws Exception {
        // Given
        User user = new User("test@email.com");

        // When
        userManagementService.updateUser(user, new UserContext());

        // Then
        verify(userDAO).updateUser(any(User.class));
    }

    @Test
    void updateUserPassword() throws Exception {
        // Given
        User user = new User("test@email.com");
        user.setPassword("password");

        // When
        userManagementService.updateUserPassword(user, new UserContext());

        // Then
        verify(userDAO).updatePassword(any(User.class));
    }

    @Test
    void deleteUser() throws Exception {
        // Given
        User user = new User("test@email.com");

        // When
        userManagementService.deleteUser(user, new UserContext());

        // Then
        verify(userDAO).deleteUser(any(User.class));
    }
}