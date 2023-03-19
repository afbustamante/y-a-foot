package net.andresbustamante.yafoot.core.web.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.core.adapters.UserManagementAdapter;
import net.andresbustamante.yafoot.core.web.config.MappingTestConfig;
import net.andresbustamante.yafoot.core.web.config.UserManagementTestConfig;
import net.andresbustamante.yafoot.core.web.config.UsersApiClientConfig;
import net.andresbustamante.yafoot.users.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

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
}
