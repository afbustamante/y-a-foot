package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Site;
import net.andresbustamante.yafoot.model.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

class SiteManagementServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private SiteManagementServiceImpl siteManagementService;

    @Mock
    private SiteDAO siteDAO;

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
        site.setNom("Test name");
        site.setAdresse("Test address");

        siteManagementService.saveSite(site, userContext);

        verify(siteDAO).saveSite(any(Site.class));
    }
}