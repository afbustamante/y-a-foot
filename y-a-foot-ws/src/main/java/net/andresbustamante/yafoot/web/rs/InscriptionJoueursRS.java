package net.andresbustamante.yafoot.web.rs;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.xs.Contexte;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.services.GestionJoueursService;
import net.andresbustamante.yafoot.util.ConfigProperties;
import net.andresbustamante.yafoot.util.ContexteUtils;
import net.andresbustamante.yafoot.web.mappers.ContexteMapper;
import net.andresbustamante.yafoot.web.mappers.JoueurMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class InscriptionJoueursRS {

    @Autowired
    private GestionJoueursService gestionJoueursService;

    private final Log log = LogFactory.getLog(InscriptionJoueursRS.class);

    /**
     * @param joueur
     * @return
     */
    @PostMapping(path = "/joueurs/gestion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> inscrireJoueur(@RequestBody Joueur joueur) {
        try {
            log.info("Demande de création d'un nouveau joueur avec l'adresse " + joueur.getEmail());
            net.andresbustamante.yafoot.model.Joueur nouveauJoueur = JoueurMapper.INSTANCE.toJoueurBean(joueur);
            boolean inscrit = gestionJoueursService.inscrireJoueur(nouveauJoueur,
                    ContexteMapper.INSTANCE.toContexteBean(new Contexte()));

            if (inscrit) {
                MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                String location = MessageFormat.format(ConfigProperties.getValue(
                        "recherche.joueurs.email.service.path"), joueur.getEmail());
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
    @PutMapping(path = "/joueurs/gestion", produces = MediaType.APPLICATION_JSON_VALUE)
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
        }
    }
}
