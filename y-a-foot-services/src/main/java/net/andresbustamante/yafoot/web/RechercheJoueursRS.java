package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.services.GestionJoueursService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author andresbustamante
 */
@RestController
public class RechercheJoueursRS {

    @Autowired
    private GestionJoueursService gestionJoueursService;

    private transient final Log log = LogFactory.getLog(RechercheJoueursRS.class);

    @GetMapping(path = "/joueurs/recherche/{email}/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Joueur> chercherJoueurParEmail(@PathVariable("email") String email) {
        Contexte contexte = new Contexte();

        try {
            Joueur joueur = gestionJoueursService.chercherJoueur(email, contexte);

            if (joueur != null) {
                return new ResponseEntity<>(joueur, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (BDDException e) {
            log.error("Erreur lors de la recherche d'un utilisateur", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
