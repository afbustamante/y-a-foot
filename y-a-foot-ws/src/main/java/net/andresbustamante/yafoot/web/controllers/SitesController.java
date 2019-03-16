package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.xs.Sites;
import net.andresbustamante.yafoot.services.RechercheSitesService;
import net.andresbustamante.yafoot.web.mappers.SiteMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author andresbustamante
 */
@RestController
@RequestMapping("/sites")
public class SitesController {

    @Autowired
    private RechercheSitesService rechercheSitesService;

    private final Logger log = LoggerFactory.getLogger(SitesController.class);

    @GetMapping(path = "", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Sites> getSitesJoueur(@RequestParam("idJoueur") Integer idJoueur) {
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
