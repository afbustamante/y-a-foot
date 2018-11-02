package net.andresbustamante.yafoot.web.rs;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.xs.Sites;
import net.andresbustamante.yafoot.services.RechercheSitesService;
import net.andresbustamante.yafoot.web.mappers.SiteMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author andresbustamante
 */
@RestController
@RequestMapping("/sites/recherche")
public class RechercheSitesRS {

    @Autowired
    private RechercheSitesService rechercheSitesService;

    private final Log log = LogFactory.getLog(RechercheSitesRS.class);

    @GetMapping(path = "/joueur/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Sites> getSitesJoueur(@PathVariable("id") Integer idJoueur) {
        try {
            List<net.andresbustamante.yafoot.model.Site> sites = rechercheSitesService.chercherSitesParJoueur(idJoueur,
                    new Contexte());

            if (CollectionUtils.isNotEmpty(sites)) {
                Sites result = new Sites();

                for (net.andresbustamante.yafoot.model.Site site : sites) {
                    result.getSite().add(SiteMapper.INSTANCE.toSiteDTO(site));
                }
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Sites(), HttpStatus.OK);
            }
        } catch (BDDException e) {
            log.error("Erreur lors de la recherche de sites par joueur", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
