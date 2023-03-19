package net.andresbustamante.yafoot.core.web.adapters;

import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.adapters.UserManagementAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Internal users adapter implementation.
 */
@Component
public class InternalUserManagementAdapterImpl implements UserManagementAdapter {

    @Value("${api.users.server.url}")
    private String usersApiUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public InternalUserManagementAdapterImpl(@Qualifier("usersRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void deleteUser(User user, UserContext context) throws DirectoryException {
        UriComponentsBuilder uriBuilder = getUriBuilder();
        uriBuilder.path("/").path(user.getEmail());

        try {
            restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE,
                    HttpEntity.EMPTY, Void.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new DirectoryException(e.getMessage());
        }
    }

    private UriComponentsBuilder getUriBuilder() {
        return UriComponentsBuilder.fromHttpUrl(usersApiUrl);
    }
}
