package net.andresbustamante.yafoot.users.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.users.exceptions.InvalidCredentialsException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.services.UserAuthenticationService;
import net.andresbustamante.yafoot.users.services.UserManagementService;
import net.andresbustamante.yafoot.users.web.config.MappingTestConfig;
import net.andresbustamante.yafoot.users.web.config.WebSecurityTestConfig;
import net.andresbustamante.yafoot.users.web.dto.Credentials;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersController.class)
@Import(UsersController.class)
@ContextConfiguration(classes = {WebSecurityTestConfig.class, MappingTestConfig.class})
class UsersControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserManagementService userManagementService;

    @MockBean
    private UserAuthenticationService userAuthenticationService;

    @Test
    void authenticateValidUser() throws Exception {
        // Given
        String email = VALID_EMAIL;
        net.andresbustamante.yafoot.users.web.dto.User user = new net.andresbustamante.yafoot.users.web.dto.User();
        user.setEmail(email);
        user.setPassword("passwd".getBytes());
        User testUser = new User(email, "passwd", "DOE", "Joe");

        given(userAuthenticationService.authenticate(any(User.class))).willReturn(testUser);

        // When
        mvc.perform(post("/users/{0}/auth-token", email)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void authenticateInvalidPassword() throws Exception {
        // Given
        String email = VALID_EMAIL;
        net.andresbustamante.yafoot.users.web.dto.User user = new net.andresbustamante.yafoot.users.web.dto.User();
        user.setEmail(email);
        user.setPassword("wrong".getBytes());

        InvalidCredentialsException exception = new InvalidCredentialsException("Error");

        given(userAuthenticationService.authenticate(any(User.class))).willThrow(exception);

        // When
        mvc.perform(post("/users/{0}/auth-token", email)
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
        net.andresbustamante.yafoot.users.web.dto.User user = new net.andresbustamante.yafoot.users.web.dto.User();
        user.setEmail(email);
        user.setPassword("wrong".getBytes());

        given(userAuthenticationService.authenticate(any(User.class))).willThrow(DirectoryException.class);

        // When
        mvc.perform(post("/users/{0}/auth-token", email)
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
        net.andresbustamante.yafoot.users.web.dto.User user = new net.andresbustamante.yafoot.users.web.dto.User();
        user.setEmail(email);
        user.setPassword("passwd".getBytes());

        InvalidCredentialsException exception = new InvalidCredentialsException("Error");

        given(userAuthenticationService.authenticate(any(User.class))).willThrow(exception);

        // When
        mvc.perform(post("/users/{0}/auth-token", email)
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
        net.andresbustamante.yafoot.users.web.dto.User user = new net.andresbustamante.yafoot.users.web.dto.User();
        user.setEmail(email);
        user.setPassword("passwd".getBytes());

        given(userAuthenticationService.authenticate(any(User.class))).willReturn(null);

        // When
        mvc.perform(post("/users/{0}/auth-token", email)
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

        given(userSearchService.findUserByToken(anyString())).willReturn(testUser);

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
        given(userSearchService.findUserByToken(anyString())).willReturn(null);

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
        given(userSearchService.findUserByToken(anyString())).willThrow(DirectoryException.class);

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
        given(userSearchService.findUserByToken(anyString())).willThrow(DirectoryException.class);

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
        given(userSearchService.findUserByToken(anyString())).willThrow(DirectoryException.class);

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

        given(userSearchService.findUserByEmail(anyString())).willReturn(testUser);

        // When
        mvc.perform(post("/users/{0}/reset-token", email)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isCreated());
    }

    @Test
    void generatePasswordResetTokenForInvalidUser() throws Exception {
        // Given
        String email = "doe.john@email.com";

        given(userSearchService.findUserByEmail(anyString())).willReturn(null);

        // When
        mvc.perform(post("/users/{0}/reset-token", email)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void generatePasswordResetTokenWhenLdapDirectoryIsUnavailable() throws Exception {
        // Given
        String email = "john.john@email.com";

        given(userSearchService.findUserByEmail(anyString())).willThrow(DirectoryException.class);

        // When
        mvc.perform(post("/users/{0}/reset-token", email)
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

        given(userSearchService.findUserByEmail(anyString())).willReturn(user);

        // When
        mvc.perform(patch("/users/{0}", email)
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

        given(userSearchService.findUserByEmail(anyString())).willReturn(user);

        // When
        mvc.perform(patch("/users/{0}", email)
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

        given(userSearchService.findUserByEmail(anyString())).willReturn(user);

        // When
        mvc.perform(patch("/users/{0}", email)
                .header(AUTHORIZATION, getAuthString(email))
                .content(objectMapper.writeValueAsString(credentials))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isForbidden());
    }
}