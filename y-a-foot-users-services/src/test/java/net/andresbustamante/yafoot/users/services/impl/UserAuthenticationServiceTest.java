package net.andresbustamante.yafoot.users.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.services.AbstractServiceTest;
import net.andresbustamante.yafoot.commons.util.JwtTokenUtils;
import net.andresbustamante.yafoot.users.exceptions.InvalidCredentialsException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserAuthenticationServiceImpl}.
 */
class UserAuthenticationServiceTest extends AbstractServiceTest {

    private static final User USR_TEST = new User("test@email.com", "password", "TEST",
            "User");

    @InjectMocks
    private UserAuthenticationServiceImpl userAuthenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Test
    void testAuthenticateInvalidUser() {
        // Given
        User invalidUser = new User("invalid.user@email.com");
        invalidUser.setPassword("pass");

        // When
        when(userRepository.authenticateUser(anyString(), anyString())).thenReturn(null);

        // Then
        assertThrows(InvalidCredentialsException.class, () -> userAuthenticationService.authenticate(invalidUser));
        verify(userRepository).authenticateUser(anyString(), anyString());
        verify(jwtTokenUtils, never()).generateToken(anyString());
    }

    @Test
    void testAuthenticateValidUser() throws ApplicationException {
        // When
        when(userRepository.authenticateUser(anyString(), anyString())).thenReturn(USR_TEST);
        when(jwtTokenUtils.generateToken(anyString())).thenReturn("token");

        User authUser = userAuthenticationService.authenticate(USR_TEST);

        // Then
        assertNotNull(authUser);
        assertNotNull(authUser.getToken());
        verify(userRepository).authenticateUser(anyString(), anyString());
        verify(jwtTokenUtils).generateToken(anyString());
    }

}
