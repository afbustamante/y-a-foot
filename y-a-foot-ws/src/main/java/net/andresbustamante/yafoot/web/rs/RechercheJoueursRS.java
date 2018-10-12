package net.andresbustamante.yafoot.web.rs;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.web.mappers.JoueurMapper;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.services.RechercheJoueursService;
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
    private RechercheJoueursService rechercheJoueursService;

    private transient final Log log = LogFactory.getLog(RechercheJoueursRS.class);

    @GetMapping(path = "/joueurs/recherche/{email}/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Joueur> chercherJoueurParEmail(@PathVariable("email") String email) {
        Contexte contexte = new Contexte();

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
