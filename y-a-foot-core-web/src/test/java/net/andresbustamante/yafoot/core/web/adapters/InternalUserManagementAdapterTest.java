package net.andresbustamante.yafoot.core.web.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.core.adapters.UserManagementAdapter;
import net.andresbustamante.yafoot.core.web.config.MappingTestConfig;
import net.andresbustamante.yafoot.core.web.config.UserManagementTestConfig;
import net.andresbustamante.yafoot.core.web.config.UsersApiClientConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MappingTestConfig.class, UsersApiClientConfig.class, UserManagementTestConfig.class})
@RestClientTest(InternalUserManagementAdapterImpl.class)
class InternalUserManagementAdapterTest {

    @Value("${api.users.server.url}")
    private String serverUrl;

    @Autowired
    private UserManagementAdapter userManagementAdapter;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser() throws Exception {
        // Given
        User user = new User();
        user.setEmail("test@email.com");
        user.setFirstName("Test");
        user.setSurname("Test");

        server.expect(requestTo(serverUrl)).andRespond(withSuccess());

        // When-Then
        assertDoesNotThrow(() -> userManagementAdapter.createUser(user, null));
    }

    @Test
    void createUserWithServerError() throws Exception {
        // Given
        User user = new User();
        user.setEmail("test@email.com");
        user.setFirstName("Test");
        user.setSurname("Test");

        server.expect(requestTo(serverUrl)).andRespond(withServerError());

        // When-Then
        assertThrows(DirectoryException.class, () -> userManagementAdapter.createUser(user, null));
    }

    @Test
    void updateUser() {
        // Given
        User user = new User();
        String email = "test@email.com";
        user.setEmail(email);
        user.setFirstName("Test");
        user.setSurname("Test");

        server.expect(requestTo(serverUrl + "/" + email)).andRespond(withSuccess());

        // When-Then
        assertDoesNotThrow(() -> userManagementAdapter.updateUser(user, null));
    }

    @Test
    void updateUserWithServerError() {
        // Given
        User user = new User();
        String email = "test@email.com";
        user.setEmail(email);
        user.setFirstName("Test");
        user.setSurname("Test");

        server.expect(requestTo(serverUrl + "/" + email)).andRespond(withServerError());

        // When-Then
        assertThrows(DirectoryException.class, () -> userManagementAdapter.updateUser(user, null));
    }

    @Test
    void deleteUser() {
        // Given
        User user = new User();
        String email = "test@email.com";
        user.setEmail(email);
        user.setFirstName("Test");
        user.setSurname("Test");

        server.expect(requestTo(serverUrl + "/" + email)).andRespond(withSuccess());

        // When-Then
        assertDoesNotThrow(() -> userManagementAdapter.deleteUser(user, null));
    }

    @Test
    void deleteUserWithServerError() {
        // Given
        User user = new User();
        String email = "test@email.com";
        user.setEmail(email);
        user.setFirstName("Test");
        user.setSurname("Test");

        server.expect(requestTo(serverUrl + "/" + email)).andRespond(withServerError());

        // When-Then
        assertThrows(DirectoryException.class, () -> userManagementAdapter.deleteUser(user, null));
    }

    @Test
    void findExistingUserByEmail() throws Exception {
        // Given
        net.andresbustamante.yafoot.users.dto.User testUser = new net.andresbustamante.yafoot.users.dto.User();
        String email = "test@mail.com";
        testUser.setEmail(email);
        testUser.setFirstName("John");
        testUser.setSurname("Doe");

        server.expect(requestTo(serverUrl + "/" + email))
                .andRespond(withSuccess(objectMapper.writeValueAsString(testUser), MediaType.APPLICATION_JSON));

        // When-Then
        assertDoesNotThrow(() -> userManagementAdapter.findUserByEmail("test@mail.com"));
    }

    @Test
    void findNewUserByEmail() throws Exception {
        // Given
        net.andresbustamante.yafoot.users.dto.User testUser = new net.andresbustamante.yafoot.users.dto.User();
        String email = "test@mail.com";
        testUser.setEmail(email);
        testUser.setFirstName("John");
        testUser.setSurname("Doe");

        server.expect(requestTo(serverUrl + "/" + email)).andRespond(withStatus(HttpStatus.NOT_FOUND));

        // When-Then
        assertThrows(DirectoryException.class, () -> userManagementAdapter.findUserByEmail("test@mail.com"));
    }

    @Test
    void findUserByEmailWithServerError() throws Exception {
        // Given
        net.andresbustamante.yafoot.users.dto.User testUser = new net.andresbustamante.yafoot.users.dto.User();
        String email = "test@mail.com";
        testUser.setEmail(email);
        testUser.setFirstName("John");
        testUser.setSurname("Doe");

        server.expect(requestTo(serverUrl + "/" + email)).andRespond(withServerError());

        // When-Then
        assertThrows(DirectoryException.class, () -> userManagementAdapter.findUserByEmail("test@mail.com"));
    }
}