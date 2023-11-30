package net.andresbustamante.yafoot.users.repository.impl;

import jakarta.ws.rs.WebApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.repository.UserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class KeycloakUserRepositoryImpl implements UserRepository {

    private final Keycloak keycloak;

    @Value("${app.keycloak.server.realm}")
    private String realm;

    public KeycloakUserRepositoryImpl(final Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public void updateUser(final User user) throws DirectoryException {
        try {
            Optional<UserRepresentation> userRepresentationOptional = findUser(user.getEmail());

            if (userRepresentationOptional.isPresent()) {
                UserRepresentation userRepresentation = userRepresentationOptional.get();

                String userId = userRepresentation.getId();

                copyUserData(user, userRepresentation);

                RealmResource realmResource = keycloak.realm(realm);
                UsersResource usersResource = realmResource.users();

                UserResource userResource = usersResource.get(userId);
                userResource.update(userRepresentation);
            }
        } catch (WebApplicationException e) {
            throw new DirectoryException(e.getMessage());
        }
    }

    @Override
    public void deleteUser(final User user) throws DirectoryException {
        try {
            Optional<UserRepresentation> userRepresentationOptional = findUser(user.getEmail());

            if (userRepresentationOptional.isPresent()) {
                UserRepresentation userRepresentation = userRepresentationOptional.get();

                String userId = userRepresentation.getId();

                RealmResource realmResource = keycloak.realm(realm);
                UsersResource usersResource = realmResource.users();

                UserResource userResource = usersResource.get(userId);
                userResource.remove();
            }
        } catch (WebApplicationException e) {
            throw new DirectoryException(e.getMessage());
        }
    }

    @Override
    public User findUserByEmail(final String email) throws DirectoryException {
        try {
            Optional<UserRepresentation> userRepresentationOptional = findUser(email);

            if (userRepresentationOptional.isPresent()) {
                User user = new User();
                copyUserData(userRepresentationOptional.get(), user);
                return user;
            }
            return null;
        } catch (WebApplicationException e) {
            throw new DirectoryException(e.getMessage());
        }
    }

    private Optional<UserRepresentation> findUser(final String email) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> directoryUsers = usersResource.searchByEmail(email, false);

        if (CollectionUtils.isNotEmpty(directoryUsers)) {
            return directoryUsers.stream().filter(u -> email.equalsIgnoreCase(u.getEmail())).findFirst();
        }

        return Optional.empty();
    }

    private void copyUserData(final User source, final UserRepresentation destination) {
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getSurname());
    }

    private void copyUserData(final UserRepresentation source, final User destination) {
        destination.setFirstName(source.getFirstName());
        destination.setSurname(source.getLastName());
    }
}
