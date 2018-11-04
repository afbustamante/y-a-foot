package net.andresbustamante.yafoot.web.rs;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.xs.Inscription;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.services.GestionMatchsService;
import net.andresbustamante.yafoot.util.ConfigProperties;
import net.andresbustamante.yafoot.util.ContexteUtils;
import net.andresbustamante.yafoot.web.mappers.InscriptionMapper;
import net.andresbustamante.yafoot.web.mappers.MatchMapper;
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
 * @author andresbustamante
 */
@RestController
public class OrganisationMatchsRS {

    @Autowired
    private GestionMatchsService gestionMatchsService;

    @PostMapping(path = "/matchs/gestion", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(path = "/matchs/inscription", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> inscrireJoueurMatch(@RequestBody Inscription inscription,
                                                       @RequestHeader HttpHeaders headers) {
        try {
            net.andresbustamante.yafoot.model.Contexte contexte = ContexteUtils.getContexte(headers);
            net.andresbustamante.yafoot.model.Inscription ins = InscriptionMapper.INSTANCE.toInscriptionBean(inscription);
            boolean succes = gestionMatchsService.inscrireJoueurMatch(ins.getJoueur(), ins.getMatch(),
                    ins.getVoiture(), contexte);

            return (succes) ? new ResponseEntity<>(true, HttpStatus.CREATED) :
                    new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        } catch (BDDException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/matchs/inscription", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> desinscrireJoueurMatch(@RequestBody Inscription inscription,
                                                          @RequestHeader HttpHeaders headers) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @PutMapping(path = "/matchs/annulation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> annulerMatch(@RequestBody Match match,
                                                @RequestHeader HttpHeaders headers) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
