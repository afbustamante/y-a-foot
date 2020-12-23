package net.andresbustamante.yafoot.web.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = {"net.andresbustamante.yafoot.web.mappers"})
public class MappingTestConfig {
}
