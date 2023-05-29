package net.andresbustamante.yafoot.users.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${app.keycloak.server.url}")
    private String serverUrl;

    @Value("${app.keycloak.server.realm}")
    private String realm;

    @Value("${app.keycloak.client.id}")
    private String clientId;

    @Value("${app.keycloak.client.secret}")
    private String clientSecret;

    @Value("${app.keycloak.client.username}")
    private String username;

    @Value("${app.keycloak.client.password}")
    private String password;

    @Bean
    public Keycloak keycloakClient() {
        // User "idm-admin" needs at least "manage-users, view-clients, view-realm, view-users" roles for
        // "realm-management"

        // Create user (requires manage-users role)

        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .build();
    }
}
