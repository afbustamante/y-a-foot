package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.config.JdbcTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JdbcTestConfig.class)
public class VoitureDAOTest {

    @Test
    public void chercherVoitureParId() throws Exception {
    }
}