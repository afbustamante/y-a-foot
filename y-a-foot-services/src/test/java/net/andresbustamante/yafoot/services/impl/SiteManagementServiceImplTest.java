package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.Site;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.services.PlayerSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SiteManagementServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private SiteManagementServiceImpl siteManagementService;

    @Mock
    private SiteDAO siteDAO;

    @Mock
    private PlayerSearchService playerSearchService;

    private UserContext userContext;

    @BeforeEach
    void setUp() {
        userContext = new UserContext();
        userContext.setUsername("test@email.com");
    }

    @Test
    void saveSite() throws DatabaseException {
        // Given
        Site site = new Site(1);
        site.setName("Test name");
        site.setAddress("Test address");
        Player player = new Player(1);

        // When
        when(playerSearchService.findPlayerByEmail(anyString())).thenReturn(player);
        siteManagementService.saveSite(site, userContext);

        // Then
        verify(siteDAO).saveSite(any(Site.class), any(Player.class));
    }
}