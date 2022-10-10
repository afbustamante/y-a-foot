package net.andresbustamante.yafoot.core.web.controllers;

import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.commons.util.JwtTokenUtils;
import net.andresbustamante.yafoot.core.adapters.UserManagementAdapter;
import net.andresbustamante.yafoot.core.web.config.JwtConfig;
import net.andresbustamante.yafoot.core.web.config.MappingTestConfig;
import net.andresbustamante.yafoot.core.web.config.WebSecurityTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.MessageFormat;

import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MappingTestConfig.class, WebSecurityTestConfig.class, JwtConfig.class})
public abstract class AbstractControllerTest {

    protected static final String VALID_EMAIL = "john.doe@email.com";

    @MockBean
    protected UserManagementAdapter userManagementAdapter;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Value("${api.config.public.url}")
    protected String apiPublicUrl;

    @BeforeEach
    void initUserDetailsService() throws Exception {
        User testUser = new User("john.doe@email.com", "passwd", "DOE", "John");
        given(userManagementAdapter.findUserByEmail(VALID_EMAIL)).willReturn(testUser);
    }

    protected String getAuthString(String email) {
        String token = jwtTokenUtils.generateToken(email);
        return MessageFormat.format("Bearer {0}", token);
    }
}
