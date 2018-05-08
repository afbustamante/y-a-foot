package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.services.GestionJoueursService;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

/**
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
            boolean inscrit = gestionJoueursService.inscrireJoueur(joueur, new Contexte());

            if (inscrit) {
                MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                headers.add("Location", joueur.getId().toString());
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
     * @param contexte
     * @return
     */
    @PutMapping(path = "/joueurs/gestion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> actualiserJoueur(@RequestBody Joueur joueur,
                                                    @RequestBody Contexte contexte) {
        try {
            boolean succes = gestionJoueursService.actualiserJoueur(joueur, contexte);
            return (succes) ? new ResponseEntity<>(true, HttpStatus.ACCEPTED) : new ResponseEntity<>(false,
                    HttpStatus.BAD_REQUEST);
        } catch (BDDException e) {
            log.error("Erreur lors de l'actualisation d'un joueur", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
