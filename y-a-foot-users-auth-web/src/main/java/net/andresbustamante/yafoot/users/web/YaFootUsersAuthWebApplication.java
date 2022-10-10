package net.andresbustamante.yafoot.users.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = {"net.andresbustamante.yafoot"})
@EnableAspectJAutoProxy
public class YaFootUsersAuthWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(YaFootUsersAuthWebApplication.class);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(YaFootUsersAuthWebApplication.class);
    }
}