package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.commons.exceptions.LdapException;
import net.andresbustamante.yafoot.commons.model.User;
import net.andresbustamante.yafoot.auth.model.enums.RolesEnum;
import net.andresbustamante.yafoot.auth.services.UserAuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link JwtUserDetailsService}
 */
@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

    @InjectMocks
    private JwtUserDetailsService userDetailsService;

    @Mock
    private UserAuthenticationService userAuthenticationService;

    @Test
    void loadUserByUsername() throws Exception {
        // Given
        String email = "john.doe@email.com";
        String passwd = "passwd";
        User testUser = new User(email, passwd, "DOE", "John");
        when(userAuthenticationService.findUserByEmail(anyString())).thenReturn(testUser);

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
        when(userAuthenticationService.findUserByEmail(anyString())).thenReturn(null);

        // When - Then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));
    }

    @Test
    void loadUserByUsernameWhenLdapDirectoryDown() throws Exception {
        // Given
        String email = "doe.john@email.com";
        when(userAuthenticationService.findUserByEmail(anyString())).thenThrow(LdapException.class);

        // When - Then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));
    }
}