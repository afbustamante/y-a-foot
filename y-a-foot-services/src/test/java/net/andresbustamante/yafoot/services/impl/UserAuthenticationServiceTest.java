package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.commons.services.AbstractServiceTest;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.InvalidCredentialsException;
import net.andresbustamante.yafoot.commons.exceptions.LdapException;
import net.andresbustamante.yafoot.auth.repository.UserRepository;
import net.andresbustamante.yafoot.commons.model.User;
import net.andresbustamante.yafoot.util.JwtTokenUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void findUserByEmail() throws LdapException {
        // When
        when(userRepository.findUserByEmail(anyString())).thenReturn(USR_TEST);

        User user = userAuthenticationService.findUserByEmail("test@email.com");

        // Then
        assertNotNull(user);
        assertEquals(USR_TEST, user);

        verify(userRepository).findUserByEmail(anyString());
    }
}