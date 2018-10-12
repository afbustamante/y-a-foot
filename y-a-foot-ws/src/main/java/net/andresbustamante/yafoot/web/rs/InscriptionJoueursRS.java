package net.andresbustamante.yafoot.web.rs;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.web.mappers.ContexteMapper;
import net.andresbustamante.yafoot.web.mappers.JoueurMapper;
import net.andresbustamante.yafoot.model.xs.Contexte;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.services.GestionJoueursService;
import net.andresbustamante.yafoot.util.ContexteUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
            net.andresbustamante.yafoot.model.Joueur nouveauJoueur = JoueurMapper.INSTANCE.toJoueurBean(joueur);
            boolean inscrit = gestionJoueursService.inscrireJoueur(nouveauJoueur,
                    ContexteMapper.INSTANCE.toContexteBean(new Contexte()));

            if (inscrit) {
                MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                headers.add("Location", nouveauJoueur.getId().toString());
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
