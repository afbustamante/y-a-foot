package net.andresbustamante.yafoot.commons.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"net.andresbustamante.yafoot.commons.util"})
@PropertySource("classpath:application.properties")
public class JwtTestConfig {
}
