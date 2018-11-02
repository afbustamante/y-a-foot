package net.andresbustamante.yafoot.web.rs;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.web.mappers.MatchMapper;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Matchs;
import net.andresbustamante.yafoot.services.RechercheMatchsService;
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
 * Web Service REST pour la recherche et consultation des matches
 *
 * @author andresbustamante
 */
@RestController
@RequestMapping("/matchs/recherche")
public class RechercheMatchsRS {

    @Autowired
    private RechercheMatchsService rechercheMatchsService;

    private final Log log = LogFactory.getLog(RechercheMatchsRS.class);

    public RechercheMatchsRS() {
    }

    @GetMapping(path = "/code/{codeMatch}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Match> getMatchParCode(@PathVariable("codeMatch") String codeMatch) {
        try {
            net.andresbustamante.yafoot.model.Match match = rechercheMatchsService.chercherMatchParCode(codeMatch,
                    new Contexte());

            return (match != null) ? new ResponseEntity<>(MatchMapper.INSTANCE.toMatchDTO(match), HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BDDException e) {
            log.error("Erreur de BD pour la recherche d'un match.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/joueur/{idJoueur}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Matchs> getMatchsJoueur(@PathVariable("idJoueur") Integer idJoueur) {
        try {
            List<net.andresbustamante.yafoot.model.Match> matchs = rechercheMatchsService.chercherMatchsJoueur(idJoueur,
                    new Contexte());

            if (CollectionUtils.isNotEmpty(matchs)) {
                Matchs result = new Matchs();

                for (net.andresbustamante.yafoot.model.Match m : matchs) {
                    result.getMatch().add(MatchMapper.INSTANCE.toMatchDTO(m));
                }
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Matchs(), HttpStatus.OK);
            }
        } catch (BDDException e) {
            log.error("Erreur de BD pour la recherche d'un match.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
