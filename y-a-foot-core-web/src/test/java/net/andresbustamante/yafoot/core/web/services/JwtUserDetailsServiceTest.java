package net.andresbustamante.yafoot.core.web.services;

import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.commons.model.enums.RolesEnum;
import net.andresbustamante.yafoot.core.adapters.UserManagementAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link JwtUserDetailsService}
 */
@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

    @InjectMocks
    private JwtUserDetailsService userDetailsService;

    @Mock
    private UserManagementAdapter userManagementAdapter;

    @Test
    void loadUserByUsername() throws Exception {
        // Given
        String email = "john.doe@email.com";
        String passwd = "";
        User testUser = new User(email, passwd, "DOE", "John");
        when(userManagementAdapter.findUserByEmail(anyString())).thenReturn(testUser);

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Then
        assertNotNull(userDetails);
        assertNotNull(userDetails.getUsername());
        assertEquals(email, userDetails.getUsername());
        assertNotNull(userDetails.getPassword());
        assertEquals(passwd, userDetails.getPassword());
        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority(RolesEnum.PLAYER.name())));
    }

    @Test
    void loadInvalidUserByUsername() throws Exception {
        // Given
        String email = "doe.john@email.com";
        when(userManagementAdapter.findUserByEmail(anyString())).thenReturn(null);

        // When - Then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));
    }

    @Test
    void loadUserByUsernameWhenLdapDirectoryDown() throws Exception {
        // Given
        String email = "doe.john@email.com";
        when(userManagementAdapter.findUserByEmail(anyString())).thenThrow(DirectoryException.class);

        // When - Then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));
    }
}