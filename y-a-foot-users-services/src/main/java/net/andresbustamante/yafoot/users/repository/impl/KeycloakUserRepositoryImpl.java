package net.andresbustamante.yafoot.users.repository.impl;

import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.repository.UserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class KeycloakUserRepositoryImpl implements UserRepository {

    private final Keycloak keycloak;

    private final Logger log = LoggerFactory.getLogger(KeycloakUserRepositoryImpl.class);

    @Value("${app.keycloak.server.realm}")
    private String realm;

    public KeycloakUserRepositoryImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public void updateUser(User user) throws DirectoryException {
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
        } catch (Exception e) {
            log.error("An error occurred while updating a user on Keycloak", e);
            throw new DirectoryException(e.getMessage());
        }
    }

    @Override
    public void deleteUser(User user) throws DirectoryException {
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
        } catch (Exception e) {
            log.error("An error occurred while deleting a user on Keycloak", e);
            throw new DirectoryException(e.getMessage());
        }
    }

    @Override
    public User findUserByEmail(String email) throws DirectoryException {
        try {
            Optional<UserRepresentation> userRepresentationOptional = findUser(email);

            if (userRepresentationOptional.isPresent()) {
                User user = new User();
                copyUserData(userRepresentationOptional.get(), user);
                return user;
            }
            return null;
        } catch (Exception e) {
            log.error("An error occurred while looking for a user on Keycloak", e);
            throw new DirectoryException(e.getMessage());
        }
    }

    private Optional<UserRepresentation> findUser(String email) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> directoryUsers = usersResource.searchByEmail(email, false);

        if (CollectionUtils.isNotEmpty(directoryUsers)) {
            return directoryUsers.stream().filter(u -> email.equalsIgnoreCase(u.getEmail())).findFirst();
        }

        return Optional.empty();
    }

    private void copyUserData(User source, UserRepresentation destination) {
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getSurname());
    }

    private void copyUserData(UserRepresentation source, User destination) {
        destination.setFirstName(source.getFirstName());
        destination.setSurname(source.getLastName());
    }
}
