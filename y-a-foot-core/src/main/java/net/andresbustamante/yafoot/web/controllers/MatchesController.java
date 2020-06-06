package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.AuthorisationException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.services.CarpoolingService;
import net.andresbustamante.yafoot.services.MatchManagementService;
import net.andresbustamante.yafoot.services.MatchSearchService;
import net.andresbustamante.yafoot.services.PlayerSearchService;
import net.andresbustamante.yafoot.web.dto.CarConfirmation;
import net.andresbustamante.yafoot.web.dto.Match;
import net.andresbustamante.yafoot.web.dto.Registration;
import net.andresbustamante.yafoot.web.mappers.BasicMatchMapper;
import net.andresbustamante.yafoot.web.mappers.CarMapper;
import net.andresbustamante.yafoot.web.mappers.MatchMapper;
import net.andresbustamante.yafoot.web.mappers.RegistrationMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.andresbustamante.yafoot.web.controllers.AbstractController.CTX_MESSAGES;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpStatus.*;

/**
 * REST service for managing and searching matches
 *
 * @author andresbustamante
 */
@RestController
@CrossOrigin(exposedHeaders = {CTX_MESSAGES})
public class MatchesController extends AbstractController implements MatchesApi {

    /* Message codes */
    private static final String UNKNOWN_MATCH_ERROR = "unknown.match.error";
    private static final String UNKNOWN_PLAYER_REGISTRATION_ERROR = "unknown.player.registration.error";
    private static final String UNKNOWN_PLAYER_ERROR = "unknown.player.error";

    private MatchSearchService matchSearchService;

    private PlayerSearchService playerSearchService;

    private MatchManagementService matchManagementService;

    private CarpoolingService carpoolingService;

    private MatchMapper matchMapper;

    private BasicMatchMapper basicMatchMapper;

    private RegistrationMapper registrationMapper;

    private CarMapper carMapper;

    @Value("${match.api.service.path}")
    private String matchApiPath;

    @Value("${match.player.api.service.path}")
    private String matchPlayerApiPath;

    private final Logger log = LoggerFactory.getLogger(MatchesController.class);

    @Autowired
    public MatchesController(MatchSearchService matchSearchService, PlayerSearchService playerSearchService,
                             CarpoolingService carpoolingService,
                             MatchManagementService matchManagementService, BasicMatchMapper basicMatchMapper,
                             MatchMapper matchMapper, RegistrationMapper registrationMapper, CarMapper carMapper,
                             HttpServletRequest request, ApplicationContext applicationContext) {
        super(request, applicationContext);
        this.matchSearchService = matchSearchService;
        this.matchManagementService = matchManagementService;
        this.playerSearchService = playerSearchService;
        this.carpoolingService = carpoolingService;
        this.matchMapper = matchMapper;
        this.basicMatchMapper = basicMatchMapper;
        this.registrationMapper = registrationMapper;
        this.carMapper = carMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<Match> loadMatchByCode(String matchCode) {
        try {
            net.andresbustamante.yafoot.model.Match match = matchSearchService.findMatchByCode(matchCode);

            return (match != null) ? ResponseEntity.ok(matchMapper.map(match)) :
                    new ResponseEntity<>(buildMessageHeader(UNKNOWN_MATCH_ERROR, null), NOT_FOUND);
        } catch (DatabaseException e) {
            log.error("Database error while looking for a match", e);
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Registration>> loadMatchRegistrations(String matchCode) {
        try {
            net.andresbustamante.yafoot.model.Match match = matchSearchService.findMatchByCode(matchCode);

            return (match != null) ? ResponseEntity.ok(registrationMapper.map(match.getRegistrations())) :
                    new ResponseEntity<>(buildMessageHeader(UNKNOWN_MATCH_ERROR, null), NOT_FOUND);
        } catch (DatabaseException e) {
            log.error("Database error while looking for a match", e);
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Match>> findMatches(@DateTimeFormat(iso = DATE) LocalDate startDate,
                                                   @DateTimeFormat(iso = DATE) LocalDate endDate) {
        try {
            UserContext ctx = getUserContext(request);

            Player player = playerSearchService.findPlayerByEmail(ctx.getUsername());

            List<net.andresbustamante.yafoot.model.Match> matches = matchSearchService.findMatchesByPlayer(player,
                    startDate, endDate);

            if (CollectionUtils.isNotEmpty(matches)) {
                List<Match> result = basicMatchMapper.map(matches);
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        } catch (DatabaseException e) {
            log.error("Database error while looking for a player's matches", e);
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            log.error("User context error for loading a player's matches", e);
            return new ResponseEntity<>(buildMessageHeader(INVALID_USER_ERROR, null), BAD_REQUEST);
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

    @CrossOrigin(exposedHeaders = {HttpHeaders.LOCATION})
    @Override
    public ResponseEntity<Void> createMatch(@Valid Match match) {
        try {
            UserContext userContext = getUserContext(request);
            net.andresbustamante.yafoot.model.Match m = matchMapper.map(match);
            matchManagementService.saveMatch(m, userContext);

            String location = MessageFormat.format(matchApiPath, m.getCode());
            return ResponseEntity.created(getLocationURI(location)).build();
        } catch (DatabaseException e) {
            log.error("Erreur lors de la création d'un match", e);
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            log.error("Erreur lors de la récupération des information du contexte", e);
            return new ResponseEntity<>(buildMessageHeader(INVALID_USER_ERROR, null), BAD_REQUEST);
        }
    }

    @CrossOrigin(exposedHeaders = {HttpHeaders.LOCATION})
    @Override
    public ResponseEntity<Void> registerPlayerToMatch(Registration registration, String matchCode) {
        try {
            UserContext userContext = getUserContext(request);
            net.andresbustamante.yafoot.model.Registration reg = registrationMapper.map(registration);

            net.andresbustamante.yafoot.model.Match match = matchSearchService.findMatchByCode(matchCode);
            Player player = playerSearchService.findPlayerByEmail(reg.getPlayer().getEmail());

            if (match == null) {
                return new ResponseEntity<>(buildMessageHeader(UNKNOWN_MATCH_ERROR, null), NOT_FOUND);
            } else if (player == null) {
                log.warn("Invalid player used while trying to register a player to a match");
                return new ResponseEntity<>(buildMessageHeader(UNKNOWN_PLAYER_ERROR,
                        new String[]{reg.getPlayer().getEmail()}), BAD_REQUEST);
            }

            matchManagementService.registerPlayer(player, match, reg.getCar(), userContext);

            String location = MessageFormat.format(matchPlayerApiPath, match.getCode(), reg.getPlayer().getId());
            return ResponseEntity.created(getLocationURI(location)).build();
        } catch (DatabaseException e) {
            log.error("Database error while trying to register a player to a match", e);
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            log.error("Application error when registering a player to a match", e);
            return new ResponseEntity<>(buildMessageHeader(INVALID_USER_ERROR, null), BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Void> updateCarForRegistration(CarConfirmation carConfirmation, String matchCode, Integer playerId) {
        try {
            net.andresbustamante.yafoot.model.Match match = matchSearchService.findMatchByCode(matchCode);

            if (match != null) {
                List<net.andresbustamante.yafoot.model.Registration> playerRegistrations = match.getRegistrations().stream().filter(
                        reg -> reg.getPlayer().getId().equals(playerId)).collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(playerRegistrations)) {
                    UserContext ctx = getUserContext(request);
                    Player player = playerRegistrations.get(0).getPlayer(); // It must be always the first and the only one

                    carpoolingService.updateCarpoolingInformation(match, player, carMapper.map(carConfirmation.getCar()),
                                carConfirmation.isConfirmed(), ctx);
                    return ResponseEntity.accepted().build();
                } else {
                    log.warn("Invalid player ID detected for player registration update");
                    return new ResponseEntity<>(buildMessageHeader(UNKNOWN_PLAYER_REGISTRATION_ERROR, null), NOT_FOUND);
                }
            } else {
                log.warn("Invalid match code detected for player registration update");
                return new ResponseEntity<>(buildMessageHeader(UNKNOWN_MATCH_ERROR, null), NOT_FOUND);
            }
        } catch (DatabaseException e) {
            log.error("Database error when updating a registration", e);
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        } catch (AuthorisationException e) {
            log.error("Authorisation problem when updating carpool details for a registration to a match", e);
            return new ResponseEntity<>(buildMessageHeader(UNAUTHORISED_USER_ERROR, null), FORBIDDEN);
        } catch (ApplicationException e) {
            log.error("Application error when updating a registration to a match", e);
            return new ResponseEntity<>(buildMessageHeader(INVALID_USER_ERROR, null), BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Void> unregisterPlayerFromMatch(String matchCode, Integer playerId) {
        try {
            UserContext userContext = getUserContext(request);

            net.andresbustamante.yafoot.model.Match match = matchSearchService.findMatchByCode(matchCode);
            Player player = playerSearchService.findPlayerById(playerId);

            if (match != null && player != null) {
                matchManagementService.unregisterPlayer(player, match, userContext);
                return ResponseEntity.noContent().build();
            } else if (match == null) {
                log.warn("Invalid match code detected for unregistering player");
                return new ResponseEntity<>(buildMessageHeader(UNKNOWN_MATCH_ERROR, null), NOT_FOUND);
            } else {
                log.warn("Invalid player ID detected for unregistering player");
                return new ResponseEntity<>(buildMessageHeader(UNKNOWN_PLAYER_REGISTRATION_ERROR, null), NOT_FOUND);
            }
        } catch (DatabaseException e) {
            log.error("Database error while unregistering a player from a match", e);
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            log.error("Application error when unregistering a player from a match", e);
            return new ResponseEntity<>(buildMessageHeader(INVALID_USER_ERROR, null), BAD_REQUEST);
        }
    }
}
