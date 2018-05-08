package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Site;
import net.andresbustamante.yafoot.services.RechercheSitesService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author andresbustamante
 */
@RestController
public class RechercheSitesRS {

    @Autowired
    private RechercheSitesService rechercheSitesService;

    private final Log log = LogFactory.getLog(RechercheSitesRS.class);

    @GetMapping(path = "/sites/recherche/joueur/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Site>> getSitesJoueur(@PathVariable("id") Integer idJoueur) {
        try {
            List<Site> sites = rechercheSitesService.chercherSitesParJoueur(idJoueur, new Contexte());
            return new ResponseEntity<>(sites, HttpStatus.OK);
        } catch (BDDException e) {
            log.error("Erreur lors de la recherche de sites par joueur", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
