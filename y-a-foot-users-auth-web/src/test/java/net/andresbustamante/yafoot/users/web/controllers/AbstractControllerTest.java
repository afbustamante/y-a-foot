package net.andresbustamante.yafoot.users.web.controllers;

import net.andresbustamante.yafoot.commons.util.JwtTokenUtils;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.services.UserSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.MessageFormat;

import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application.yml")
public abstract class AbstractControllerTest {

    protected static final String VALID_EMAIL = "john.doe@email.com";

    @MockBean
    protected UserSearchService userSearchService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Value("${api.config.public.url}")
    protected String apiPublicUrl;

    @BeforeEach
    void initUserDetailsService() throws Exception {
        User testUser = new User("john.doe@email.com", "passwd", "DOE", "John");
        given(userSearchService.findUserByEmail(VALID_EMAIL)).willReturn(testUser);
    }

    protected String getAuthString(String email) {
        String token = jwtTokenUtils.generateToken(email);
        return MessageFormat.format("Bearer {0}", token);
    }
}
