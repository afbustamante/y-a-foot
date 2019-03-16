package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Matchs;
import net.andresbustamante.yafoot.services.GestionMatchsService;
import net.andresbustamante.yafoot.services.RechercheMatchsService;
import net.andresbustamante.yafoot.web.mappers.MatchMapper;
import net.andresbustamante.yafoot.web.util.ContexteUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Value("${recherche.matchs.code.service.path}")
    private String pathRechercheMatchsParCode;

    private final Logger log = LoggerFactory.getLogger(MatchsController.class);

    public MatchsController() {
    }

    @GetMapping(path = "/{codeMatch}", produces = MediaType.APPLICATION_XML_VALUE)
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
                String location = MessageFormat.format(pathRechercheMatchsParCode, m.getCode());
                headersResponse.add(HttpHeaders.LOCATION, location);
                return new ResponseEntity<>(m.getCode(), headersResponse, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
            }
        } catch (BDDException e) {
            log.error("Erreur lors de la création d'un match", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/annulation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> annulerMatch(@RequestBody Match match,
                                                @RequestHeader HttpHeaders headers) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
