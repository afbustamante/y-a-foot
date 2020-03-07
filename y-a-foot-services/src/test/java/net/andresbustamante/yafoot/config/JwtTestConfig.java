package net.andresbustamante.yafoot.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "net.andresbustamante.yafoot.util")
@PropertySource("classpath:application.properties")
public class JwtTestConfig {
}
