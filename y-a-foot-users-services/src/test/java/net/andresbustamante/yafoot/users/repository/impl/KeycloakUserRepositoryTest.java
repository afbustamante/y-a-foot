package net.andresbustamante.yafoot.users.repository.impl;

import net.andresbustamante.yafoot.users.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakUserRepositoryTest {

    @InjectMocks
    private KeycloakUserRepositoryImpl keycloakUserRepository;

    @Mock
    private Keycloak keycloak;

    private RealmResource realmResource;

    private UsersResource usersResource;

    @BeforeEach
    public void setUp() {
        realmResource = Mockito.mock(RealmResource.class);
        usersResource = Mockito.mock(UsersResource.class);

        String email = "test@email.com";
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("id");
        userRepresentation.setEnabled(true);
        userRepresentation.setEmail(email);
        userRepresentation.setFirstName("Test");
        userRepresentation.setLastName("User");

        when(keycloak.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.searchByEmail(eq(email), anyBoolean())).thenReturn(List.of(userRepresentation));

        ReflectionTestUtils.setField(keycloakUserRepository, "realm", "test");
    }

    @Test
    void testFindUserByEmail() throws Exception {
        // When
        var result = keycloakUserRepository.findUserByEmail("test@email.com");

        // Then
        assertNotNull(result);
    }

    @Test
    void testUpdateUser() throws Exception {
        // Given
        String email = "test@email.com";

        User storedUser = new User(email);
        storedUser.setFirstName("Rob");
        storedUser.setSurname("Usr");

        User updatedUser = new User(email);
        updatedUser.setFirstName("Bob");
        updatedUser.setSurname("User");

        UserResource userResource = Mockito.mock(UserResource.class);
        when(usersResource.get(eq("id"))).thenReturn(userResource);

        // When - Then
        assertDoesNotThrow(() -> keycloakUserRepository.updateUser(updatedUser));
    }

    @Test
    void deleteUser() {
        // Given
        String email = "test@email.com";
        User user = new User(email);

        UserResource userResource = Mockito.mock(UserResource.class);
        when(usersResource.get(eq("id"))).thenReturn(userResource);

        // When-Then
        assertDoesNotThrow(() -> keycloakUserRepository.deleteUser(user));
    }
}
