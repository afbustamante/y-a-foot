package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.web.dto.Site;
import net.andresbustamante.yafoot.services.SiteSearchService;
import net.andresbustamante.yafoot.web.mappers.SiteMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * REST controller for sites related operations
 *
 * @author andresbustamante
 */
@RestController
public class SitesController extends AbstractController implements SitesApi {

    private SiteSearchService siteSearchService;

    private SiteMapper siteMapper;

    private final Logger log = LoggerFactory.getLogger(SitesController.class);

    @Autowired
    public SitesController(SiteSearchService siteSearchService, SiteMapper siteMapper) {
        this.siteSearchService = siteSearchService;
        this.siteMapper = siteMapper;
    }

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.empty();
    }

    @Override
    public ResponseEntity<List<Site>> loadSitesByPlayer(Integer playerId) {
        try {
            List<net.andresbustamante.yafoot.model.Site> sites = siteSearchService.findSitesByPlayer(playerId,
                    new UserContext());

            List<Site> result = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(sites)) {
                for (net.andresbustamante.yafoot.model.Site site : sites) {
                    result.add(siteMapper.map(site));
                }
            }
            return ResponseEntity.ok(result);
        } catch (DatabaseException e) {
            log.error("Erreur lors de la recherche de sites par joueur", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }
}
