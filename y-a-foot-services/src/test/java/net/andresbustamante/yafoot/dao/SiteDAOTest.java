package net.andresbustamante.yafoot.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Site;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static org.junit.jupiter.api.Assertions.*;

@DatabaseSetup(value = "classpath:datasets/matchesDataset.xml")
@DatabaseTearDown(value = "classpath:datasets/matchesDataset.xml", type = DELETE_ALL)
class SiteDAOTest extends AbstractDAOTest {

    @Autowired
    private SiteDAO siteDAO;

    @Test
    void findSitesByPlayer() throws Exception {
        // Given
        Player player1 = new Player(1);

        // When
        List<Site> sites = siteDAO.findSitesByPlayer(player1);

        // Then
        assertNotNull(sites);
        assertEquals(1, sites.size());
        assertEquals(1, sites.get(0).getId().intValue());
        assertEquals("1234567890", sites.get(0).getPhoneNumber());
    }

    @Test
    void findSiteById() throws Exception {
        // Given
        // When
        Site site = siteDAO.findSiteById(1);

        // Then
        assertNotNull(site);
        assertEquals("1234567890", site.getPhoneNumber());
        assertEquals("9 Rue du sport", site.getAddress());
        assertEquals("Foot-à-5 de la ville", site.getName());
    }

    @Test
    void saveSite() throws Exception {
        // Given
        Site newSite = new Site("Nouveau site", "123 Rue du site", "0412345678", null);
        Player player1 = new Player(1);

        // When
        int numLines = siteDAO.saveSite(newSite, player1);

        // Then
        assertEquals(1, numLines);
        assertNotNull(newSite.getId());
        assertTrue(newSite.getId() > 0);
    }
}