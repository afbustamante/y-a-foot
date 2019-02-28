package net.andresbustamante.yafoot.web.rs;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.xs.Inscription;
import net.andresbustamante.yafoot.services.GestionMatchsService;
import net.andresbustamante.yafoot.util.ConfigProperties;
import net.andresbustamante.yafoot.util.ContexteUtils;
import net.andresbustamante.yafoot.web.mappers.InscriptionMapper;
import net.andresbustamante.yafoot.web.mappers.MatchMapper;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Matchs;
import net.andresbustamante.yafoot.services.RechercheMatchsService;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.List;

/**
 * Web Service REST pour la recherche et consultation des matches
 *
 * @author andresbustamante
 */
@RestController
@RequestMapping("/matchs")
public class MatchsController {

    @Autowired
    private RechercheMatchsService rechercheMatchsService;

    @Autowired
    private GestionMatchsService gestionMatchsService;

    private final Log log = LogFactory.getLog(MatchsController.class);

    public MatchsController() {
    }

    @GetMapping(path = "/code/{codeMatch}", produces = MediaType.APPLICATION_XML_VALUE)
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

    @GetMapping(path = "/joueur/{idJoueur}", produces = MediaType.APPLICATION_XML_VALUE)
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

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> creerMatch(@RequestBody Match match, @RequestHeader HttpHeaders headers) {
        try {
            net.andresbustamante.yafoot.model.Contexte contexte = ContexteUtils.getContexte(headers);
            net.andresbustamante.yafoot.model.Match m = MatchMapper.INSTANCE.toMatchBean(match);
            boolean isMatchCree = gestionMatchsService.creerMatch(m, contexte);

            if (isMatchCree) {
                MultiValueMap<String, String> headersResponse = new LinkedMultiValueMap<>();
                String location = MessageFormat.format(ConfigProperties.getValue(
                        "recherche.matchs.code.service.path"), m.getCode());
                headersResponse.add(HttpHeaders.LOCATION, location);
                return new ResponseEntity<>(m.getCode(), headersResponse, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
            }
        } catch (BDDException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/annulation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> annulerMatch(@RequestBody Match match,
                                                @RequestHeader HttpHeaders headers) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}