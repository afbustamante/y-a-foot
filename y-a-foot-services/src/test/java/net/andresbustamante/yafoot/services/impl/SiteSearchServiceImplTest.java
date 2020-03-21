package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Site;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SiteSearchServiceImplTest extends AbstractServiceTest {

    private final Site site1 = new Site(1);
    private final Site site2 = new Site(2);

    @InjectMocks
    private SiteSearchServiceImpl rechercheSitesService;

    @Mock
    private SiteDAO siteDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void chercherSitesParJoueur() throws Exception {
        // Given
        Player player1 = new Player(1);
        UserContext ctx = new UserContext();

        // When
        when(siteDAO.findSitesByPlayer(any(Player.class))).thenReturn(Arrays.asList(site1, site2));
        List<Site> sites = rechercheSitesService.findSitesByPlayer(player1);

        // Then
        verify(siteDAO, times(1)).findSitesByPlayer(any(Player.class));
        assertNotNull(sites);
        assertEquals(2, sites.size());
    }
}