package net.andresbustamante.yafoot.users.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = {"net.andresbustamante.yafoot"})
@EnableAspectJAutoProxy
public class YaFootUsersAdminWebApplication extends SpringBootServletInitializer {

    /**
     * Main method to start the application.
     *
     * @param args Application parameters
     */
    public static void main(String[] args) {
        SpringApplication.run(YaFootUsersAdminWebApplication.class);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(YaFootUsersAdminWebApplication.class);
    }
}
