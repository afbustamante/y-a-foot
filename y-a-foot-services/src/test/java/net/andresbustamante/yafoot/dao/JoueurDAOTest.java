package net.andresbustamante.yafoot.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/applicationContext.xml")
public class JoueurDAOTest {

    @Autowired
    JoueurDAO joueurDAO;

    @Test
    public void isJoueurInscrit() throws Exception {
        boolean isInscrit = joueurDAO.isJoueurInscrit("nonInscrit@email.com");
        assertFalse(isInscrit);

        isInscrit = joueurDAO.isJoueurInscrit("afbusta@yahoo.com");
        assertTrue(isInscrit);
    }

    @Test
    public void chercherJoueurParId() throws Exception {
    }

    @Test
    public void creerJoueur() throws Exception {
    }

    @Test
    public void actualiserJoueur() throws Exception {
    }
}