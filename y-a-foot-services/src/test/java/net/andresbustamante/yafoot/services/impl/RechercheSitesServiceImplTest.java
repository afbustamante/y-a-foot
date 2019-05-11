package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Site;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RechercheSitesServiceImplTest extends AbstractServiceTest {

    private final Site site1 = new Site(1);
    private final Site site2 = new Site(2);

    @InjectMocks
    private RechercheSitesServiceImpl rechercheSitesService;

    @Mock
    private SiteDAO siteDAO;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void chercherSitesParJoueur() throws Exception {
        // Given
        int idJoueur = 1;
        Contexte ctx = new Contexte();

        // When
        when(siteDAO.chercherSitesPourJoueur(anyInt())).thenReturn(Arrays.asList(site1, site2));
        List<Site> sites = rechercheSitesService.chercherSitesParJoueur(idJoueur, ctx);

        // Then
        verify(siteDAO, times(1)).chercherSitesPourJoueur(1);
        assertNotNull(sites);
        assertEquals(2, sites.size());
    }
}