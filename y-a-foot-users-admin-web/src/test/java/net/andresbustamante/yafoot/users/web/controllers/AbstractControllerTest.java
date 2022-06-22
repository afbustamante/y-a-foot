package net.andresbustamante.yafoot.users.web.controllers;

import net.andresbustamante.yafoot.users.services.UserSearchService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application.yml")
public abstract class AbstractControllerTest {

    protected static final String VALID_EMAIL = "john.doe@email.com";

    @MockBean
    protected UserSearchService userSearchService;

}
