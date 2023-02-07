package net.andresbustamante.yafoot.core.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.commons.config.DbUnitTestConfig;
import net.andresbustamante.yafoot.commons.config.JdbcTestConfig;
import net.andresbustamante.yafoot.commons.dao.AbstractDaoIntegrationTest;
import net.andresbustamante.yafoot.core.config.MyBatisTestConfig;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Site;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {JdbcTestConfig.class, MyBatisTestConfig.class, DbUnitTestConfig.class})
@DatabaseSetup(value = "classpath:datasets/matches/t_player_match.csv")
@DatabaseTearDown(value = "classpath:datasets/matches/t_player_match.csv", type = DELETE_ALL)
class SiteDaoIT extends AbstractDaoIntegrationTest {

    @Autowired
    private SiteDao siteDAO;

    @Test
    void findSitesByPlayer() throws Exception {
        // Given
        Player player1 = new Player(101);

        // When
        List<Site> sites = siteDAO.findSitesByPlayer(player1);

        // Then
        assertNotNull(sites);
        assertEquals(1, sites.size());
        assertEquals(101, sites.get(0).getId().intValue());
        assertEquals("1234567890", sites.get(0).getPhoneNumber());
    }

    @Test
    void findSiteById() throws Exception {
        // Given
        // When
        Site site = siteDAO.findSiteById(101);

        // Then
        assertNotNull(site);
        assertEquals("1234567890", site.getPhoneNumber());
        assertEquals("9 Rue du sport", site.getAddress());
        assertEquals("Foot-à-5 de la ville", site.getName());
    }

    @Test
    void saveSite() throws Exception {
        // Given
        Site newSite = new Site();
        newSite.setName("Nouveau site");
        newSite.setAddress("123 Rue du site");
        newSite.setPhoneNumber("0412345678");
        Player player1 = new Player(101);

        // When
        int numLines = siteDAO.saveSite(newSite, player1);

        // Then
        assertEquals(1, numLines);
        assertNotNull(newSite.getId());
        assertTrue(newSite.getId() > 0);
    }
}
