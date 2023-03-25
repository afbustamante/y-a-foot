package net.andresbustamante.yafoot.users.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.services.UserManagementService;
import net.andresbustamante.yafoot.users.services.UserSearchService;
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
