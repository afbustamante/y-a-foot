package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.config.JdbcTestConfig;
import net.andresbustamante.yafoot.config.MyBatisConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JdbcTestConfig.class, MyBatisConfig.class})
public class MatchDAOTest {

    @Test
    public void isCodeExistant() throws Exception {
    }

    @Test
    public void chercherMatchParCode() throws Exception {
    }

    @Test
    public void chercherMatchsParJoueur() throws Exception {
    }

    @Test
    public void chercherMatchParId() throws Exception {
    }

    @Test
    public void creerMatch() throws Exception {
    }

    @Test
    public void inscrireJoueurMatch() throws Exception {
    }

    @Test
    public void isJoueurInscritMatch() throws Exception {
    }
}