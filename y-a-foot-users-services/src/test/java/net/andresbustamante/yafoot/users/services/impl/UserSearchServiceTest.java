package net.andresbustamante.yafoot.users.services.impl;

import net.andresbustamante.yafoot.commons.services.AbstractServiceUnitTest;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserSearchServiceImpl}.
 */
class UserSearchServiceTest extends AbstractServiceUnitTest {

    private static final User USR_TEST = new User("test@email.com", "password", "TEST",
            "User");

    @InjectMocks
    private UserSearchServiceImpl userSearchService;

    @Mock
    private UserRepository userRepository;

    @Test
    void findUserByEmail() throws Exception {
        // When
        when(userRepository.findUserByEmail(anyString())).thenReturn(USR_TEST);

        User user = userSearchService.findUserByEmail("test@email.com");

        // Then
        assertNotNull(user);
        assertEquals(USR_TEST, user);

        verify(userRepository).findUserByEmail(anyString());
    }

    @Test
    void findUserByToken() throws Exception {
        // When
        when(userRepository.findUserByToken(anyString())).thenReturn(USR_TEST);

        User user = userSearchService.findUserByToken("TOKEN");

        // Then
        assertNotNull(user);
        assertEquals(USR_TEST, user);

        verify(userRepository).findUserByToken(anyString());
    }
}
