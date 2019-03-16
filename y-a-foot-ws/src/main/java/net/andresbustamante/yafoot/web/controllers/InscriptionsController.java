package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.xs.Inscription;
import net.andresbustamante.yafoot.services.GestionMatchsService;
import net.andresbustamante.yafoot.web.mappers.InscriptionMapper;
import net.andresbustamante.yafoot.web.util.ContexteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Web Service REST pour la gestion des inscriptions aux matches
 *
 * @author andresbustamante
 */
@RestController
@RequestMapping("/inscriptions")
public class InscriptionsController {

    @Autowired
    private GestionMatchsService gestionMatchsService;

    private final Logger log = LoggerFactory.getLogger(InscriptionsController.class);

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> inscrireJoueurMatch(@RequestBody Inscription inscription,
                                                       @RequestHeader HttpHeaders headers) {
        log.info("Traitement de nouvelle demande d'inscription");

        try {
            net.andresbustamante.yafoot.model.Contexte contexte = ContexteUtils.getContexte(headers);
            net.andresbustamante.yafoot.model.Inscription ins = InscriptionMapper.INSTANCE.toInscriptionBean(inscription);
            boolean succes = gestionMatchsService.inscrireJoueurMatch(ins.getJoueur(), ins.getMatch(),
                    ins.getVoiture(), contexte);

            if (succes) {
                log.info("Le joueur a ete inscrit");
                return new ResponseEntity<>(true, HttpStatus.CREATED);
            } else {
                log.warn("Le joueur n'a pas pu etre inscrit");
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (BDDException e) {
            log.error("Erreur de base de données", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
