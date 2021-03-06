package net.andresbustamante.yafoot.commons.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class JdbcTestConfig {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.username}")
    private String jdbcUsername;
    @Value("${spring.datasource.password}")
    private String jdbcPassword;
    @Value("${spring.datasource.driver-class-name}")
    private String jdbcDriverClassName;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setDriverClassName(jdbcDriverClassName);
        dataSource.setUsername(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        return dataSource;
    }

    @PostConstruct
    public void updateDatabase() {
        Flyway flyway = Flyway.configure().dataSource(dataSource()).load();

        // Start the migration
        flyway.migrate();
    }
}
