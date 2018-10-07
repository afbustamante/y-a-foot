package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.services.RechercheMatchsService;
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
 * Web Service REST pour la recherche et consultation des matches
 *
 * @author andresbustamante
 */
@RestController
public class RechercheMatchsRS {

    @Autowired
    private RechercheMatchsService rechercheMatchsService;

    private final Log log = LogFactory.getLog(RechercheMatchsRS.class);

    public RechercheMatchsRS() {
    }

    @GetMapping(path = "/matchs/recherche/code/{codeMatch}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Match> getMatchParCode(@PathVariable("codeMatch") String codeMatch) {
        try {
            Match match = rechercheMatchsService.chercherMatchParCode(codeMatch, new Contexte());

            return (match != null) ? new ResponseEntity<>(match, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BDDException e) {
            log.error("Erreur de BD pour la recherche d'un match.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/matchs/recherche/joueur/{idJoueur}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Match[]> getMatchsJoueur(@PathVariable("idJoueur") Integer idJoueur) {
        try {
            List<Match> matchs = rechercheMatchsService.chercherMatchsJoueur(idJoueur, new Contexte());

            return (matchs != null) ? new ResponseEntity<>(matchs.toArray(new Match[]{}), HttpStatus.OK) :
                    new ResponseEntity<>(new Match[]{}, HttpStatus.OK);
        } catch (BDDException e) {
            log.error("Erreur de BD pour la recherche d'un match.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
