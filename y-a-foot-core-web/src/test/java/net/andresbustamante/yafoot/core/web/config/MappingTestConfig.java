package net.andresbustamante.yafoot.core.web.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = {"net.andresbustamante.yafoot.commons.web.mappers", "net.andresbustamante.yafoot.core.web.mappers"})
public class MappingTestConfig {
}
