package net.andresbustamante.yafoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "net.andresbustamante.yafoot")
@EnableAspectJAutoProxy
public class YaFootCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(YaFootCoreApplication.class, args);
    }
}