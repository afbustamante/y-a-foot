package net.andresbustamante.yafoot.core.web.controllers;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public abstract class AbstractControllerTest {

    protected static final String VALID_EMAIL = "john.doe@email.com";
}
