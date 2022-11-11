package net.andresbustamante.yafoot.core.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.web.controllers.AbstractController;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import net.andresbustamante.yafoot.core.services.SiteManagementService;
import net.andresbustamante.yafoot.core.services.SiteSearchService;
import net.andresbustamante.yafoot.web.dto.Site;
import net.andresbustamante.yafoot.core.web.mappers.SiteMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * REST controller for sites related operations.
 *
 * @author andresbustamante
 */
@RestController
public class SitesController extends AbstractController implements SitesApi {

    private final SiteSearchService siteSearchService;
    private final SiteManagementService siteManagementService;
    private final PlayerSearchService playerSearchService;
    private final SiteMapper siteMapper;

    @Value("${api.sites.one.path}")
    private String siteApiPath;

    @Autowired
    public SitesController(SiteSearchService siteSearchService, SiteManagementService siteManagementService,
                           PlayerSearchService playerSearchService, SiteMapper siteMapper, HttpServletRequest request,
                           ObjectMapper objectMapper, ApplicationContext applicationContext) {
        super(request, objectMapper, applicationContext);
        this.siteSearchService = siteSearchService;
        this.siteManagementService = siteManagementService;
        this.playerSearchService = playerSearchService;
        this.siteMapper = siteMapper;
    }

    @Override
    public ResponseEntity<List<Site>> loadSites() {
        try {
            UserContext ctx = getUserContext();
            Player player = playerSearchService.findPlayerByEmail(ctx.getUsername());
            List<net.andresbustamante.yafoot.core.model.Site> sites = siteSearchService.findSitesByPlayer(player);

            List<Site> result = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(sites)) {
                result.addAll(siteMapper.map(sites));
            }
            return ResponseEntity.ok(result);
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }

    @CrossOrigin(exposedHeaders = {HttpHeaders.LOCATION})
    @Override
    public ResponseEntity<Void> addNewSite(Site site) {
        try {
            UserContext userContext = getUserContext();
            long id = siteManagementService.saveSite(siteMapper.map(site), userContext);

            String location = MessageFormat.format(siteApiPath, id);
            return ResponseEntity.created(getLocationURI(location)).build();
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }
}
