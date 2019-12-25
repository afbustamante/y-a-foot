package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Matches;
import net.andresbustamante.yafoot.services.MatchManagementService;
import net.andresbustamante.yafoot.services.MatchSearchService;
import net.andresbustamante.yafoot.web.mappers.MatchMapper;
import net.andresbustamante.yafoot.web.util.ContextUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.time.ZoneId;
import java.util.List;

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

    private MatchManagementService matchManagementService;

    private MatchMapper matchMapper;

    private HttpServletRequest request;

    @Value("${matches.bycode.api.service.path}")
    private String pathRechercheMatchsParCode;

    private final Logger log = LoggerFactory.getLogger(MatchesController.class);

    @Autowired
    public MatchesController(MatchSearchService matchSearchService, MatchManagementService matchManagementService,
                             MatchMapper matchMapper, HttpServletRequest request) {
        this.matchSearchService = matchSearchService;
        this.matchManagementService = matchManagementService;
        this.matchMapper = matchMapper;
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

    public ResponseEntity<Matches> loadMatchesByPlayer(Integer playerId, Integer userId, String timezone) {
        try {
            UserContext ctx = new UserContext(userId);
            ctx.setTimezone(ZoneId.of(timezone));

            List<net.andresbustamante.yafoot.model.Match> matchs = matchSearchService.findMatchesByPlayer(playerId,
                    ctx);

            if (CollectionUtils.isNotEmpty(matchs)) {
                Matches result = new Matches();

                for (net.andresbustamante.yafoot.model.Match m : matchs) {
                    result.getMatch().add(matchMapper.map(m));
                }
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.ok(new Matches());
            }
        } catch (DatabaseException e) {
            log.error("Erreur de BD pour la recherche d'un match.", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Long> createMatch(Match match, Integer userId) {
        try {
            UserContext userContext = ContextUtils.getUserContext(request);
            net.andresbustamante.yafoot.model.Match m = matchMapper.map(match);
            boolean isMatchCree = matchManagementService.saveMatch(m, userContext);

            if (isMatchCree) {
                String location = MessageFormat.format(pathRechercheMatchsParCode, m.getCode());
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
}
