package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.commons.services.AbstractServiceUnitTest;
import net.andresbustamante.yafoot.core.dao.PlayerDao;
import net.andresbustamante.yafoot.core.dao.SiteDao;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Site;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SiteSearchServiceTest extends AbstractServiceUnitTest {

    private final Site site1 = new Site(1);
    private final Site site2 = new Site(2);

    @InjectMocks
    private SiteSearchServiceImpl rechercheSitesService;

    @Mock
    private SiteDao siteDAO;

    @Mock
    private PlayerDao playerDao;

    @Test
    void findSitesByPlayer() throws Exception {
        // Given
        Player player1 = new Player(1);

        // When
        when(playerDao.findPlayerByEmail(anyString())).thenReturn(player1);
        when(siteDAO.findSitesByPlayer(any(Player.class))).thenReturn(Arrays.asList(site1, site2));
        List<Site> sites = rechercheSitesService.findSites(new UserContext("player1@email.com"));

        // Then
        verify(siteDAO).findSitesByPlayer(any(Player.class));
        assertNotNull(sites);
        assertEquals(2, sites.size());
    }

    @Test
    void findSitesByUnregisteredPlayer() throws Exception {
        // Given
        // When
        when(playerDao.findPlayerByEmail(anyString())).thenReturn(null);
        List<Site> sites = rechercheSitesService.findSites(new UserContext("player1@email.com"));

        // Then
        verify(playerDao).findPlayerByEmail(anyString());
        assertNotNull(sites);
        assertTrue(sites.isEmpty());
    }
}
