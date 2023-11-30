package net.andresbustamante.yafoot.core.web.adapters;

import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.core.web.mappers.UserMapper;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.adapters.UserManagementAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
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

    private final UserMapper userMapper;

    private final RestTemplate restTemplate;

    @Autowired
    public InternalUserManagementAdapterImpl(
            final UserMapper userMapper, @Qualifier("usersRestTemplate") final RestTemplate restTemplate) {
        this.userMapper = userMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    @Async
    public void updateUser(final User user, final UserContext context) throws DirectoryException {
        UriComponentsBuilder uriBuilder = getUriBuilder();
        uriBuilder.path("/").path(user.getEmail());

        HttpEntity<net.andresbustamante.yafoot.users.web.dto.UserForm> body = new HttpEntity<>(userMapper.map(user));

        try {
            restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, body, Void.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new DirectoryException(e.getMessage());
        }
    }


    @Override
    public void deleteUser(final User user, final UserContext context) throws DirectoryException {
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
