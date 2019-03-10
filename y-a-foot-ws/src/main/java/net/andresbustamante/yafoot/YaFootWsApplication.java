package net.andresbustamante.yafoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "net.andresbustamante.yafoot")
public class YaFootWsApplication {

    public static void main(String[] args) {
        SpringApplication.run(YaFootWsApplication.class, args);
    }
}
