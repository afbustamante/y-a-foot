package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.config.JdbcTestConfig;
import net.andresbustamante.yafoot.config.MyBatisConfig;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JdbcTestConfig.class, MyBatisConfig.class})
public abstract class AbstractDAOTest {

    @Autowired
    private DataSource dataSource;

    @Before
    public void updateDatabase() throws Exception {
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();

        // Start the migration
        flyway.migrate();
    }
}