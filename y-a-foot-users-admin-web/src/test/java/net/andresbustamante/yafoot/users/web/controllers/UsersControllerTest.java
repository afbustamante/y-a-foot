package net.andresbustamante.yafoot.users.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.services.UserManagementService;
import net.andresbustamante.yafoot.users.services.UserSearchService;
import net.andresbustamante.yafoot.users.web.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {UsersController.class, ObjectMapper.class},
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UsersControllerTest extends AbstractControllerTest {

    private static final String VALID_EMAIL = "john.doe@email.com";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserManagementService userManagementService;

    @MockBean
    private UserSearchService userSearchService;

    @MockBean
    private UserMapper userMapper;

    @Test
    void updateValidUser() throws Exception {
        // Given
        net.andresbustamante.yafoot.users.web.dto.User user = new net.andresbustamante.yafoot.users.web.dto.User();
        user.setEmail(VALID_EMAIL);
        user.setFirstName("Roger");
        user.setSurname("Federer");

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
    void deleteUserWhenActiveDirectoryIsUnavailable() throws Exception {
        // Given
        given(userSearchService.findUserByEmail(anyString())).willThrow(new DirectoryException(""));

        // When
        mvc.perform(delete("/users/" + VALID_EMAIL)
                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }
}
