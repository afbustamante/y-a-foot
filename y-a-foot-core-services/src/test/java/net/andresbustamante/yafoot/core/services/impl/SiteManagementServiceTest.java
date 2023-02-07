package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.services.AbstractServiceUnitTest;
import net.andresbustamante.yafoot.core.dao.SiteDao;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Site;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

class SiteManagementServiceTest extends AbstractServiceUnitTest {

    @InjectMocks
    private SiteManagementServiceImpl siteManagementService;

    @Mock
    private SiteDao siteDAO;

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
