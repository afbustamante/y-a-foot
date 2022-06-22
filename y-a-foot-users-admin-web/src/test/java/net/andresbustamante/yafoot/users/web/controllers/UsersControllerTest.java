package net.andresbustamante.yafoot.users.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.commons.model.enums.RolesEnum;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.services.UserManagementService;
import net.andresbustamante.yafoot.users.web.config.MappingTestConfig;
import net.andresbustamante.yafoot.users.web.config.WebSecurityTestConfig;
import net.andresbustamante.yafoot.users.web.dto.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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

    @Test
    void createValidUser() throws Exception {
        // Given
        net.andresbustamante.yafoot.users.web.dto.User user = new net.andresbustamante.yafoot.users.web.dto.User();
        user.setEmail(VALID_EMAIL);
        user.setFirstName("Roger");
        user.setSurname("Federer");
        user.setMainRole(Role.PLAYER);
        user.setPassword("passwd".getBytes());

        // When
        mvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION));
    }

    @Test
    void createUserWhileLdapDirectoryIsUnavailable() throws Exception {
        // Given
        net.andresbustamante.yafoot.users.web.dto.User user = new net.andresbustamante.yafoot.users.web.dto.User();
        user.setEmail(VALID_EMAIL);
        user.setFirstName("Roger");
        user.setSurname("Federer");
        user.setMainRole(Role.PLAYER);
        user.setPassword("passwd".getBytes());

        doThrow(new DirectoryException("")).when(userManagementService).createUser(
                any(User.class), any(RolesEnum.class), any(UserContext.class));

        // When
        mvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateValidUser() throws Exception {
        // Given
        net.andresbustamante.yafoot.users.web.dto.User user = new net.andresbustamante.yafoot.users.web.dto.User();
        user.setEmail(VALID_EMAIL);
        user.setFirstName("Roger");
        user.setSurname("Federer");
        user.setPassword("newPass".getBytes());

        User storedUser = new User();
        storedUser.setFirstName("Roger");
        storedUser.setSurname("Fdrr");
        storedUser.setEmail(VALID_EMAIL);

        given(userSearchService.findUserByEmail(anyString())).willReturn(storedUser);

        // When
        mvc.perform(put("/users/" + VALID_EMAIL)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isAccepted());
    }

    @Test
    void updateInvalidUser() throws Exception {
        // Given
        net.andresbustamante.yafoot.users.web.dto.User user = new net.andresbustamante.yafoot.users.web.dto.User();
        user.setEmail(VALID_EMAIL);
        user.setFirstName("Roger");
        user.setSurname("Federer");
        user.setPassword("newPass".getBytes());

        given(userSearchService.findUserByEmail(anyString())).willReturn(null);

        // When
        mvc.perform(put("/users/" + VALID_EMAIL)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserWhenLdapDirectoryIsUnavailable() throws Exception {
        // Given
        net.andresbustamante.yafoot.users.web.dto.User user = new net.andresbustamante.yafoot.users.web.dto.User();
        user.setEmail(VALID_EMAIL);
        user.setFirstName("Roger");
        user.setSurname("Federer");
        user.setPassword("newPass".getBytes());

        given(userSearchService.findUserByEmail(anyString())).willThrow(new DirectoryException(""));

        // When
        mvc.perform(put("/users/" + VALID_EMAIL)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteValidUser() throws Exception {
        // Given
        User storedUser = new User();
        storedUser.setFirstName("Roger");
        storedUser.setSurname("Federer");
        storedUser.setEmail(VALID_EMAIL);

        given(userSearchService.findUserByEmail(anyString())).willReturn(storedUser);

        // When
        mvc.perform(delete("/users/" + VALID_EMAIL)
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteInvalidUser() throws Exception {
        // Given
        given(userSearchService.findUserByEmail(anyString())).willReturn(null);

        // When
        mvc.perform(delete("/users/" + VALID_EMAIL)
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUserWhenLdapDirectoryIsUnavailable() throws Exception {
        // Given
        given(userSearchService.findUserByEmail(anyString())).willThrow(new DirectoryException(""));

        // When
        mvc.perform(delete("/users/" + VALID_EMAIL)
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }
}