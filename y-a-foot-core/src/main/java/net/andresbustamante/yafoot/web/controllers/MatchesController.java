package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.services.PlayerSearchService;
import net.andresbustamante.yafoot.web.dto.Match;
import net.andresbustamante.yafoot.web.dto.Registration;
import net.andresbustamante.yafoot.services.MatchManagementService;
import net.andresbustamante.yafoot.services.MatchSearchService;
import net.andresbustamante.yafoot.web.mappers.MatchMapper;
import net.andresbustamante.yafoot.web.mappers.RegistrationMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Web Service REST pour la recherche et consultation des matches
 *
 * @author andresbustamante
 */
@RestController
public class MatchesController extends AbstractController implements MatchesApi {

    private MatchSearchService matchSearchService;

    private PlayerSearchService playerSearchService;

    private MatchManagementService matchManagementService;

    private MatchMapper matchMapper;

    private RegistrationMapper registrationMapper;

    private HttpServletRequest request;

    @Value("${matches.bycode.api.service.path}")
    private String matchByCodeApiPath;

    private final Logger log = LoggerFactory.getLogger(MatchesController.class);

    @Autowired
    public MatchesController(MatchSearchService matchSearchService, PlayerSearchService playerSearchService,
                             MatchManagementService matchManagementService,
                             MatchMapper matchMapper, RegistrationMapper registrationMapper, HttpServletRequest request) {
        this.matchSearchService = matchSearchService;
        this.matchManagementService = matchManagementService;
        this.playerSearchService = playerSearchService;
        this.matchMapper = matchMapper;
        this.registrationMapper = registrationMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<Match> loadMatchByCode(String matchCode) {
        try {
            net.andresbustamante.yafoot.model.Match match = matchSearchService.findMatchByCode(matchCode);

            return (match != null) ? ResponseEntity.ok(matchMapper.map(match)) : ResponseEntity.notFound().build();
        } catch (DatabaseException e) {
            log.error("Erreur de BD pour la recherche d'un match.", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<Match>> loadMatchesByPlayer(String email) {
        try {
            UserContext ctx = getUserContext(request);

            Player player = playerSearchService.findPlayerByEmail(email);

            List<net.andresbustamante.yafoot.model.Match> matchs = matchSearchService.findMatchesByPlayer(player,
                    ctx);

            if (CollectionUtils.isNotEmpty(matchs)) {
                List<Match> result = new ArrayList<>();

                for (net.andresbustamante.yafoot.model.Match m : matchs) {
                    result.add(matchMapper.map(m));
                }
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        } catch (DatabaseException e) {
            log.error("Erreur de BD pour la recherche d'un match.", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        } catch (ApplicationException e) {
            log.error("Error while loading context information", e);
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.of(request);
    }

    @Override
    public ResponseEntity<Integer> createMatch(Match match) {
        try {
            UserContext userContext = getUserContext(request);
            net.andresbustamante.yafoot.model.Match m = matchMapper.map(match);
            boolean isMatchCree = matchManagementService.saveMatch(m, userContext);

            if (isMatchCree) {
                String location = MessageFormat.format(matchByCodeApiPath, m.getCode());
                return ResponseEntity.created(getLocationURI(location)).build();
            } else {
                return ResponseEntity.status(BAD_REQUEST).build();
            }
        } catch (DatabaseException e) {
            log.error("Erreur lors de la création d'un match", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<Void> registerPlayerToMatch(Registration registration, String matchCode) {
        try {
            UserContext userContext = getUserContext(request);
            net.andresbustamante.yafoot.model.Inscription ins = registrationMapper.map(registration);

            net.andresbustamante.yafoot.model.Match match = matchSearchService.findMatchByCode(matchCode);

            if (match == null) {
                return ResponseEntity.notFound().build();
            }

            boolean succes = matchManagementService.joinMatch(ins.getPlayer(), match,
                    ins.getVoiture(), userContext);

            if (succes) {
                log.info("Player registered to match");
                String location = MessageFormat.format(matchByCodeApiPath + "/players/{0}", ins.getPlayer().getId());
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
    public ResponseEntity<Void> unregisterPlayerFromMatch(String matchCode, Integer playerId) {
        try {
            UserContext userContext = getUserContext(request);

            net.andresbustamante.yafoot.model.Match match = matchSearchService.findMatchByCode(matchCode);

            if (match != null) {
                Player player = new Player(playerId);
                matchManagementService.quitMatch(player, match, userContext);
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
