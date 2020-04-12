package net.andresbustamante.yafoot.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.Site;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static org.junit.jupiter.api.Assertions.*;

@DatabaseSetup(value = "classpath:datasets/matchsDataset.xml")
@DatabaseTearDown(value = "classpath:datasets/matchsDataset.xml", type = DELETE_ALL)
class SiteDAOTest extends AbstractDAOTest {

    @Autowired
    private SiteDAO siteDAO;

    @Test
    void chercherSitesParJoueur() throws Exception {
        // Given
        Player player1 = new Player(1);

        // When
        List<Site> sites = siteDAO.findSitesByPlayer(player1);

        // Then
        assertNotNull(sites);
        assertEquals(1, sites.size());
        assertEquals(1, sites.get(0).getId().intValue());
        assertEquals("1234567890", sites.get(0).getTelephone());
    }

    @Test
    void chercherSiteParId() throws Exception {
        // Given
        // When
        Site site = siteDAO.findSiteById(1);

        // Then
        assertNotNull(site);
        assertEquals("1234567890", site.getTelephone());
        assertEquals("9 Rue du sport", site.getAdresse());
        assertEquals("Foot-à-5 de la ville", site.getNom());
    }

    @Test
    void creerSite() throws Exception {
        // Given
        Site nouveauSite = new Site("Nouveau site", "123 Rue du site", "0412345678", null);

        // When
        siteDAO.saveSite(nouveauSite);

        // Then
        assertNotNull(nouveauSite.getId());
        assertTrue(nouveauSite.getId() > 0);
    }
}