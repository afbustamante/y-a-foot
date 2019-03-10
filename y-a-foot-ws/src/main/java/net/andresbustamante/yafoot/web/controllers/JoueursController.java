package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.xs.Contexte;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.services.GestionJoueursService;
import net.andresbustamante.yafoot.services.RechercheJoueursService;
import net.andresbustamante.yafoot.web.util.ContexteUtils;
import net.andresbustamante.yafoot.web.mappers.ContexteMapper;
import net.andresbustamante.yafoot.web.mappers.JoueurMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

/**
 * Service REST de gestion des inscriptions des joueurs dans l'application
 *
 * @author andresbustamante
 */
@RestController
@RequestMapping("/joueurs")
public class JoueursController {

    @Autowired
    private GestionJoueursService gestionJoueursService;

    @Autowired
    private RechercheJoueursService rechercheJoueursService;

    @Value("${recherche.joueurs.email.service.path}")
    private String pathRechercheJoueursParAdresseMail;

    private final Log log = LogFactory.getLog(JoueursController.class);

    /**
     * @param joueur
     * @return
     */
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> inscrireJoueur(@RequestBody Joueur joueur) {
        try {
            log.info("Demande de création d'un nouveau joueur avec l'adresse " + joueur.getEmail());
            net.andresbustamante.yafoot.model.Joueur nouveauJoueur = JoueurMapper.INSTANCE.toJoueurBean(joueur);
            boolean inscrit = gestionJoueursService.inscrireJoueur(nouveauJoueur,
                    ContexteMapper.INSTANCE.toContexteBean(new Contexte()));

            if (inscrit) {
                MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                String location = MessageFormat.format(pathRechercheJoueursParAdresseMail, joueur.getEmail());
                headers.add(HttpHeaders.LOCATION, location);
                return new ResponseEntity<>(true, headers, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (BDDException e) {
            log.error("Erreur lors de l'inscription d'un joueur", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param joueur
     * @param headers
     * @return
     */
    @PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> actualiserJoueur(@RequestBody Joueur joueur,
                                                    @RequestHeader HttpHeaders headers) {
        try {
            net.andresbustamante.yafoot.model.Contexte contexte = ContexteUtils.getContexte(headers);
            boolean succes = gestionJoueursService.actualiserJoueur(JoueurMapper.INSTANCE.toJoueurBean(joueur), contexte);
            return (succes) ? new ResponseEntity<>(true, HttpStatus.ACCEPTED) : new ResponseEntity<>(false,
                    HttpStatus.BAD_REQUEST);
        } catch (BDDException e) {
            log.error("Erreur lors de l'actualisation d'un joueur", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{email}/email", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Joueur> chercherJoueurParEmail(@PathVariable("email") String email) {
        net.andresbustamante.yafoot.model.Contexte contexte = new net.andresbustamante.yafoot.model.Contexte();

        try {
            net.andresbustamante.yafoot.model.Joueur joueur = rechercheJoueursService.chercherJoueur(email, contexte);

            if (joueur != null) {
                return new ResponseEntity<>(JoueurMapper.INSTANCE.toJoueurDTO(joueur), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (BDDException e) {
            log.error("Erreur lors de la recherche d'un utilisateur", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
