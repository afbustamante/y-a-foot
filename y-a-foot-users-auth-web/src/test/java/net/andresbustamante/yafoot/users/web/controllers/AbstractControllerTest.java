package net.andresbustamante.yafoot.users.web.controllers;

import net.andresbustamante.yafoot.commons.util.JwtTokenUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.MessageFormat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application.yml")
public abstract class AbstractControllerTest {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    /**
     * Builds a new token and an Authorization text for testing.
     *
     * @param email Email to use for the token
     * @return Authorization header value
     */
    protected String getAuthString(String email) {
        String token = jwtTokenUtils.generateToken(email);
        return MessageFormat.format("Bearer {0}", token);
    }
}
