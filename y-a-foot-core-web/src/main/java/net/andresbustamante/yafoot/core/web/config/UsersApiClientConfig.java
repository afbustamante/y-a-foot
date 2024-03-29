package net.andresbustamante.yafoot.core.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UsersApiClientConfig {

    @Value("${api.users.client.username}")
    private String clientUsername;

    @Value("${api.users.client.password}")
    private String clientPassword;

    /**
     * Builds the RestTemplate object to use for the users REST API.
     *
     * @param builder Builder to use
     * @return RestTemplate configured to contact the users API
     */
    @Bean(name = "usersRestTemplate")
    public RestTemplate usersRestTemplate(final RestTemplateBuilder builder) {
        return builder.basicAuthentication(clientUsername, clientPassword).build();
    }
}
