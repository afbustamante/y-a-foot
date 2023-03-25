package net.andresbustamante.yafoot.core.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "net.andresbustamante.yafoot")
@EnableAspectJAutoProxy
@EnableAsync
public class YaFootCoreWebApplication extends SpringBootServletInitializer {

    /**
     * Main start method for the web app.
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(YaFootCoreWebApplication.class);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(YaFootCoreWebApplication.class);
    }
}
