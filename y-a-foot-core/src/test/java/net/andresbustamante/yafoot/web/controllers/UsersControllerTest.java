package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.exceptions.InvalidCredentialsException;
import net.andresbustamante.yafoot.commons.exceptions.LdapException;
import net.andresbustamante.yafoot.commons.model.User;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.services.MessagingService;
import net.andresbustamante.yafoot.auth.services.UserManagementService;
import net.andresbustamante.yafoot.web.dto.Credentials;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersController.class)
class UsersControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserManagementService userManagementService;

    @MockBean
    private MessagingService messagingService;

    @Test
    void authenticateValidUser() throws Exception {
        // Given
        String email = VALID_EMAIL;
        net.andresbustamante.yafoot.web.dto.User user = new net.andresbustamante.yafoot.web.dto.User();
        user.setEmail(email);
        user.setPassword("passwd".getBytes());
        User testUser = new User(email, "passwd", "DOE", "Joe");

        given(userAuthenticationService.authenticate(any(User.class))).willReturn(testUser);

        // When
        mvc.perform(put("/users/{0}/auth", email)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void authenticateInvalidPassword() throws Exception {
        // Given
        String email = VALID_EMAIL;
        net.andresbustamante.yafoot.web.dto.User user = new net.andresbustamante.yafoot.web.dto.User();
        user.setEmail(email);
        user.setPassword("wrong".getBytes());

        InvalidCredentialsException exception = new InvalidCredentialsException("Error");

        given(userAuthenticationService.authenticate(any(User.class))).willThrow(exception);

        // When
        mvc.perform(put("/users/{0}/auth", email)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticateUserWhileLdapDirectoryIsUnavailable() throws Exception {
        // Given
        String email = VALID_EMAIL;
        net.andresbustamante.yafoot.web.dto.User user = new net.andresbustamante.yafoot.web.dto.User();
        user.setEmail(email);
        user.setPassword("wrong".getBytes());

        given(userAuthenticationService.authenticate(any(User.class))).willThrow(LdapException.class);

        // When
        mvc.perform(put("/users/{0}/auth", email)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void authenticateInvalidUsername() throws Exception {
        // Given
        String email = "doe.john@email.com";
        net.andresbustamante.yafoot.web.dto.User user = new net.andresbustamante.yafoot.web.dto.User();
        user.setEmail(email);
        user.setPassword("passwd".getBytes());

        InvalidCredentialsException exception = new InvalidCredentialsException("Error");

        given(userAuthenticationService.authenticate(any(User.class))).willThrow(exception);

        // When
        mvc.perform(put("/users/{0}/auth", email)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticateUnknownUser() throws Exception {
        // Given
        String email = "doe.john@email.com";
        net.andresbustamante.yafoot.web.dto.User user = new net.andresbustamante.yafoot.web.dto.User();
        user.setEmail(email);
        user.setPassword("passwd".getBytes());

        given(userAuthenticationService.authenticate(any(User.class))).willReturn(null);

        // When
        mvc.perform(put("/users/{0}/auth", email)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void findUserByValidToken() throws Exception {
        // Given
        String email = "john.doe@email.com";
        User testUser = new User(email, "passwd", "DOE", "Joe");

        given(userAuthenticationService.findUserByToken(anyString())).willReturn(testUser);

        // When
        mvc.perform(get("/users?token={0}", "ABCDEF0123456789")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findUserByInvalidToken() throws Exception {
        // Given
        given(userAuthenticationService.findUserByToken(anyString())).willReturn(null);

        // When
        mvc.perform(get("/users?token={0}", "ABCDEF0123456789")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void findUserByTokenWhileLdapDirectoryIsDownWithSupportedLocale() throws Exception {
        // Given
        given(userAuthenticationService.findUserByToken(anyString())).willThrow(LdapException.class);

        // When
        mvc.perform(get("/users?token={0}", "ABCDEF0123456789")
                .header(ACCEPT_LANGUAGE, "es-ES,es;q=0.8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findUserByTokenWhileLdapDirectoryIsDownWithUnsupportedLocale() throws Exception {
        // Given
        given(userAuthenticationService.findUserByToken(anyString())).willThrow(LdapException.class);

        // When
        mvc.perform(get("/users?token={0}", "ABCDEF0123456789")
                .header(ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findUserByTokenWhileLdapDirectoryIsUnavailable() throws Exception {
        // Given
        given(userAuthenticationService.findUserByToken(anyString())).willThrow(LdapException.class);

        // When
        mvc.perform(get("/users?token={0}", "ABCDEF0123456789")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void generatePasswordResetTokenForValidUser() throws Exception {
        // Given
        String email = "doe.john@email.com";
        User testUser = new User(email, "passwd", "DOE", "Joe");

        given(userAuthenticationService.findUserByEmail(anyString())).willReturn(testUser);

        // When
        mvc.perform(post("/users/{0}/token", email)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isCreated());
    }

    @Test
    void generatePasswordResetTokenForInvalidUser() throws Exception {
        // Given
        String email = "doe.john@email.com";

        when(userAuthenticationService.findUserByEmail(anyString())).thenReturn(null);

        // When
        mvc.perform(post("/users/{0}/token", email)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void generatePasswordResetTokenWhenLdapDirectoryIsUnavailable() throws Exception {
        // Given
        String email = "john.john@email.com";

        given(userAuthenticationService.findUserByEmail(anyString())).willThrow(LdapException.class);

        // When
        mvc.perform(post("/users/{0}/token", email)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateUserCredentials() throws Exception {
        // Given
        String email = "john.doen@email.com";
        User user = new User(email, "passwd", "DOE", "Joe");
        Credentials credentials = new Credentials()
                .username(email)
                .oldPassword("passwd".getBytes())
                .password("newPasswd".getBytes());

        given(userAuthenticationService.findUserByEmail(anyString())).willReturn(user);

        // When
        mvc.perform(patch("/users/{0}/details", email)
                .header(AUTHORIZATION, getAuthString(email))
                .content(objectMapper.writeValueAsString(credentials))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isAccepted());

        verify(userManagementService).updateUserPassword(any(User.class), any(UserContext.class));
    }

    @Test
    void updateUserCredentialsWithValidationToken() throws Exception {
        // Given
        String email = VALID_EMAIL;
        User user = new User(email, "passwd", "DOE", "Joe");
        Credentials credentials = new Credentials()
                .username(email)
                .password("newPasswd".getBytes())
                .validationToken("ABCDEF12345678");

        given(userAuthenticationService.findUserByEmail(anyString())).willReturn(user);

        // When
        mvc.perform(patch("/users/{0}/details", email)
                .header(AUTHORIZATION, getAuthString(email))
                .content(objectMapper.writeValueAsString(credentials))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isAccepted());

        verify(userManagementService).resetUserPassword(any(User.class), anyString());
    }

    @Test
    void updateUserCredentialsWrongUser() throws Exception {
        // Given
        String email = "doe.john@email.com";
        Credentials credentials = new Credentials()
                .username("john.doe@email.com")
                .oldPassword("passwd".getBytes())
                .password("newPasswd".getBytes());
        User user = new User(email, "passwd", "DOE", "Joe");

        given(userAuthenticationService.findUserByEmail(anyString())).willReturn(user);

        // When
        mvc.perform(patch("/users/{0}/details", email)
                .header(AUTHORIZATION, getAuthString(email))
                .content(objectMapper.writeValueAsString(credentials))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isForbidden());
    }
}