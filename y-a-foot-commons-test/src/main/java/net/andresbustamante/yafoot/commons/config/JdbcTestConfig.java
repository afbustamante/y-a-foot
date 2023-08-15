package net.andresbustamante.yafoot.commons.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Test configuration for JDBC.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class JdbcTestConfig {

    /**
     * Connection URL.
     */
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    /**
     * Connection username.
     */
    @Value("${spring.datasource.username}")
    private String jdbcUsername;

    /**
     * Connection password.
     */
    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    /**
     * Connection driver class.
     */
    @Value("${spring.datasource.driver-class-name}")
    private String jdbcDriverClassName;

    /**
     * Datasource bean for testing.
     *
     * @return Datasource to use for tests
     */
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setDriverClassName(jdbcDriverClassName);
        dataSource.setUsername(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        return dataSource;
    }

    /**
     * Tells Flyway to create or update de database model.
     */
    @PostConstruct
    public void updateDatabase() {
        Flyway flyway = Flyway.configure().dataSource(dataSource()).load();

        // Start the migration
        flyway.migrate();
    }
}
