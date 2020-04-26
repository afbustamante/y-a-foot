package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.services.PlayerSearchService;
import net.andresbustamante.yafoot.services.SiteManagementService;
import net.andresbustamante.yafoot.web.dto.Site;
import net.andresbustamante.yafoot.services.SiteSearchService;
import net.andresbustamante.yafoot.web.mappers.SiteMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.andresbustamante.yafoot.web.controllers.AbstractController.CTX_MESSAGES;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * REST controller for sites related operations
 *
 * @author andresbustamante
 */
@RestController
@CrossOrigin(exposedHeaders = {CTX_MESSAGES})
public class SitesController extends AbstractController implements SitesApi {

    private SiteSearchService siteSearchService;

    private SiteManagementService siteManagementService;

    private PlayerSearchService playerSearchService;

    private SiteMapper siteMapper;

    @Value("${site.api.service.path}")
    private String siteApiPath;

    private final Logger log = LoggerFactory.getLogger(SitesController.class);

    @Autowired
    public SitesController(SiteSearchService siteSearchService, SiteManagementService siteManagementService,
                           PlayerSearchService playerSearchService, SiteMapper siteMapper, HttpServletRequest request) {
        this.siteSearchService = siteSearchService;
        this.siteManagementService = siteManagementService;
        this.playerSearchService = playerSearchService;
        this.siteMapper = siteMapper;
        this.request = request;
    }

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.of(request);
    }

    @Override
    public ResponseEntity<List<Site>> loadSites() {
        try {
            UserContext ctx = getUserContext(request);
            Player player = playerSearchService.findPlayerByEmail(ctx.getUsername());
            List<net.andresbustamante.yafoot.model.Site> sites = siteSearchService.findSitesByPlayer(player);

            List<Site> result = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(sites)) {
                for (net.andresbustamante.yafoot.model.Site site : sites) {
                    result.add(siteMapper.map(site));
                }
            }
            return ResponseEntity.ok(result);
        } catch (DatabaseException e) {
            log.error("Database error while looking for a player's list of available sites", e);
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            log.error("Invalid user context", e);
            return new ResponseEntity<>(buildMessageHeader(INVALID_USER_ERROR, null), BAD_REQUEST);
        }
    }

    @CrossOrigin(exposedHeaders = {HttpHeaders.LOCATION})
    @Override
    public ResponseEntity<Void> addNewSite(Site site) {
        try {
            UserContext userContext = getUserContext(request);
            long id = siteManagementService.saveSite(siteMapper.map(site), userContext);

            String location = MessageFormat.format(siteApiPath, id);
            return ResponseEntity.created(getLocationURI(location)).build();
        } catch (DatabaseException e) {
            log.error("Database error while creating a new site", e);
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            log.error("User context error for creating a new site", e);
            return new ResponseEntity<>(buildMessageHeader(INVALID_USER_ERROR, null), BAD_REQUEST);
        }
    }
}
