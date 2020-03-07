package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.config.JwtTestConfig;
import net.andresbustamante.yafoot.config.LdapConfig;
import net.andresbustamante.yafoot.config.LdapTestConfig;
import net.andresbustamante.yafoot.exceptions.InvalidCredentialsException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.ldap.UserDAO;
import net.andresbustamante.yafoot.model.User;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import net.andresbustamante.yafoot.services.UserAuthenticationService;
import net.andresbustamante.yafoot.util.JwtTokenUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LdapTestConfig.class, LdapConfig.class, JwtTestConfig.class})
class UserAuthenticationServiceImplTest {

    private static final User USR_TEST = new User("test@email.com", "password", "TEST",
            "User");

    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @BeforeEach
    void setUp() throws Exception {
        userAuthenticationService = new UserAuthenticationServiceImpl(jwtTokenUtils, userDAO);
        userDAO.saveUser(USR_TEST, RolesEnum.PLAYER);
    }

    @AfterEach
    void tearDown() throws Exception {
        userDAO.deleteUser(USR_TEST, new RolesEnum[]{RolesEnum.PLAYER});
    }

    @Test
    void testAuthenticateInvalidUser() {
        // Given
        User invalidUser = new User("invalid.user@email.com");
        invalidUser.setPassword("pass");

        // Then
        assertThrows(InvalidCredentialsException.class, () -> userAuthenticationService.authenticate(invalidUser));
    }

    @Test
    void testAuthenticateValidUser() throws InvalidCredentialsException {
        // When
        String authToken = userAuthenticationService.authenticate(USR_TEST);

        // Then
        assertNotNull(authToken);
    }

    @Test
    void findUserByEmail() throws LdapException {
        // When
        User user = userAuthenticationService.findUserByEmail("test@email.com");

        // Then
        assertNotNull(user);
        assertEquals(USR_TEST, user);
    }
}