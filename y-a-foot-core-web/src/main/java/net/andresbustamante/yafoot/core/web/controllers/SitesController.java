package net.andresbustamante.yafoot.core.web.controllers;

import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.commons.web.controllers.AbstractController;
import net.andresbustamante.yafoot.core.services.SiteManagementService;
import net.andresbustamante.yafoot.core.services.SiteSearchService;
import net.andresbustamante.yafoot.core.web.mappers.SiteMapper;
import net.andresbustamante.yafoot.web.dto.Site;
import net.andresbustamante.yafoot.web.dto.SiteForm;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    private final SiteMapper siteMapper;

    @Value("${api.sites.one.path}")
    private String siteApiPath;

    public SitesController(
            SiteSearchService siteSearchService, SiteManagementService siteManagementService, SiteMapper siteMapper) {
        this.siteSearchService = siteSearchService;
        this.siteManagementService = siteManagementService;
        this.siteMapper = siteMapper;
    }

    @Override
    public ResponseEntity<List<Site>> loadSites() {
        try {
            UserContext ctx = getUserContext();
            List<net.andresbustamante.yafoot.core.model.Site> sites = siteSearchService.findSites(ctx);

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
    public ResponseEntity<Void> addNewSite(SiteForm site) {
        try {
            UserContext userContext = getUserContext();
            long id = siteManagementService.saveSite(siteMapper.map(site), userContext);

            String location = String.format(siteApiPath, id);
            return ResponseEntity.created(getLocationURI(location)).build();
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }
}
