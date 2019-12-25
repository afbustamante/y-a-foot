package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.xs.Registration;
import net.andresbustamante.yafoot.services.MatchManagementService;
import net.andresbustamante.yafoot.services.MatchSearchService;
import net.andresbustamante.yafoot.web.mappers.RegistrationMapper;
import net.andresbustamante.yafoot.web.util.ContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Web Service REST pour la gestion des inscriptions aux matches
 *
 * @author andresbustamante
 */
@RestController
public class RegistrationsController extends AbstractController implements RegistrationsApi {

    private MatchManagementService matchManagementService;

    private MatchSearchService matchSearchService;

    private RegistrationMapper registrationMapper;

    private HttpServletRequest request;

    private final Logger log = LoggerFactory.getLogger(RegistrationsController.class);

    @Autowired
    public RegistrationsController(MatchManagementService matchManagementService, MatchSearchService matchSearchService,
                                   RegistrationMapper registrationMapper, HttpServletRequest request) {
        this.matchManagementService = matchManagementService;
        this.matchSearchService = matchSearchService;
        this.registrationMapper = registrationMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<Void> registerPlayerToMatch(Registration registration, Integer userId) {
        log.debug("Traitement de nouvelle demande d'inscription");

        try {
            UserContext userContext = ContextUtils.getUserContext(request);
            net.andresbustamante.yafoot.model.Inscription ins = registrationMapper.map(registration);
            boolean succes = matchManagementService.joinMatch(ins.getJoueur(), ins.getMatch(),
                    ins.getVoiture(), userContext);

            if (succes) {
                log.info("Le joueur a ete inscrit");
                String location = MessageFormat.format("/players/{0}", ins.getJoueur().getEmail());
                return ResponseEntity.created(getLocationURI(location)).build();
            } else {
                log.warn("Le joueur n'a pas pu etre inscrit");
                return ResponseEntity.status(BAD_REQUEST).build();
            }
        } catch (DatabaseException e) {
            log.error("Erreur de base de données", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<Void> unregisterPlayerFromMatch(String matchCode, Integer userId) {
        try {
            UserContext userContext = ContextUtils.getUserContext(request);

            Match match = matchSearchService.findMatchByCode(matchCode);

            if (match != null) {
                Joueur joueur = new Joueur(userContext.getUserId());
                matchManagementService.quitMatch(joueur, match, userContext);
                return ResponseEntity.noContent().build();
            } else {
                log.warn("Invalid match code detected for unregistering player");
                return ResponseEntity.status(BAD_REQUEST).build();
            }
        } catch (DatabaseException e) {
            log.error("Erreur de base de données", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

}
