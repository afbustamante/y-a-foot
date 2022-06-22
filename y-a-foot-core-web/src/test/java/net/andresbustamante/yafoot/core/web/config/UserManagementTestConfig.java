package net.andresbustamante.yafoot.core.web.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@TestConfiguration
@ComponentScan(basePackages = "net.andresbustamante.yafoot.core.web.adapters")
public class UserManagementTestConfig {
}
