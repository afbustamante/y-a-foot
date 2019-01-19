package net.andresbustamante.yafoot.web.rs;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.xs.Inscription;
import net.andresbustamante.yafoot.services.GestionMatchsService;
import net.andresbustamante.yafoot.util.ContexteUtils;
import net.andresbustamante.yafoot.web.mappers.InscriptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Web Service REST pour la gestion des inscriptions aux matches
 *
 * @author andresbustamante
 */
@RestController
@RequestMapping("/inscriptions")
public class InscriptionsController {

    @Autowired
    private GestionMatchsService gestionMatchsService;

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
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

}
