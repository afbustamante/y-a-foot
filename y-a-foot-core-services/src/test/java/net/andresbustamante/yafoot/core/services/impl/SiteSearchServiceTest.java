package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.services.AbstractServiceTest;
import net.andresbustamante.yafoot.core.dao.SiteDAO;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Site;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SiteSearchServiceTest extends AbstractServiceTest {

    private final Site site1 = new Site(1);
    private final Site site2 = new Site(2);

    @InjectMocks
    private SiteSearchServiceImpl rechercheSitesService;

    @Mock
    private SiteDAO siteDAO;

    @Test
    void findSitesByPlayer() throws Exception {
        // Given
        Player player1 = new Player(1);

        // When
        when(siteDAO.findSitesByPlayer(any(Player.class))).thenReturn(Arrays.asList(site1, site2));
        List<Site> sites = rechercheSitesService.findSitesByPlayer(player1);

        // Then
        verify(siteDAO, times(1)).findSitesByPlayer(any(Player.class));
        assertNotNull(sites);
        assertEquals(2, sites.size());
    }
}