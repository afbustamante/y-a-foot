package net.andresbustamante.yafoot.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:config.properties")
public class FlywayConfig {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void migrateDb() {
        // Create the Flyway instance and point it to the database
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();

        // Start the migration
        flyway.migrate();
    }
}
