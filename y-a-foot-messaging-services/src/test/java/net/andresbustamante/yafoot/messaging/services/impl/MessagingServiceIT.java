package net.andresbustamante.yafoot.messaging.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.services.AbstractServiceIntegrationTest;
import net.andresbustamante.yafoot.messaging.config.FreemarkerTestConfig;
import net.andresbustamante.yafoot.messaging.config.MessagingTestConfig;
import net.andresbustamante.yafoot.messaging.services.MessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link MessagingServiceImpl}.
 */
@ContextConfiguration(classes = {MessagingTestConfig.class, FreemarkerTestConfig.class})
class MessagingServiceIT extends AbstractServiceIntegrationTest {

    @Autowired
    private JavaMailSender mailSender;

    private MessagingService messagingService;

    @BeforeEach
    public void setUp() {
        messagingService = new MessagingServiceImpl(mailSender);
    }

    @Test
    void sendEmail() {
        assertDoesNotThrow(() -> messagingService.sendEmail("root@localhost", "subject", "content"));
    }

    @Test
    void sendEmailToInvalidDestination() {
        assertThrows(ApplicationException.class, () -> messagingService.sendEmail("not-an-email", "subject",
                "content"));
    }
}
