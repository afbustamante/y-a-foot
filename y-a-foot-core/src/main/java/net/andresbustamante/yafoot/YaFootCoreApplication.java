package net.andresbustamante.yafoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "net.andresbustamante.yafoot")
@EnableAspectJAutoProxy
public class YaFootCoreApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(YaFootCoreApplication.class);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(YaFootCoreApplication.class);
    }
}
