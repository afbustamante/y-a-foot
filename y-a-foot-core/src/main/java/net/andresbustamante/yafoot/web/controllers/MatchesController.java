package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.UserNotAuthorisedException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.services.CarpoolingService;
import net.andresbustamante.yafoot.services.MatchManagementService;
import net.andresbustamante.yafoot.services.MatchSearchService;
import net.andresbustamante.yafoot.services.PlayerSearchService;
import net.andresbustamante.yafoot.web.dto.Car;
import net.andresbustamante.yafoot.web.dto.CarConfirmation;
import net.andresbustamante.yafoot.web.dto.Match;
import net.andresbustamante.yafoot.web.dto.Registration;
import net.andresbustamante.yafoot.web.mappers.CarMapper;
import net.andresbustamante.yafoot.web.mappers.MatchMapper;
import net.andresbustamante.yafoot.web.mappers.RegistrationMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpStatus.*;

/**
 * REST service for managing and searching matches
 *
 * @author andresbustamante
 */
@RestController
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

    private RegistrationMapper registrationMapper;

    private CarMapper carMapper;

    @Value("${match.api.service.path}")
    private String matchApiPath;

    @Value("${match.player.api.service.path}")
    private String matchPlayerApiPath;

    @Autowired
    public MatchesController(MatchSearchService matchSearchService, PlayerSearchService playerSearchService,
                             CarpoolingService carpoolingService,
                             MatchManagementService matchManagementService,
                             MatchMapper matchMapper, RegistrationMapper registrationMapper, CarMapper carMapper,
                             HttpServletRequest request, ApplicationContext applicationContext) {
        super(request, applicationContext);
        this.matchSearchService = matchSearchService;
        this.matchManagementService = matchManagementService;
        this.playerSearchService = playerSearchService;
        this.carpoolingService = carpoolingService;
        this.matchMapper = matchMapper;
        this.registrationMapper = registrationMapper;
        this.carMapper = carMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<Match> loadMatchByCode(String matchCode) {
        try {
            net.andresbustamante.yafoot.model.Match match = matchSearchService.findMatchByCode(matchCode);

            if (match != null) {
                return ResponseEntity.ok(matchMapper.map(match));
            } else {
                throw new ResponseStatusException(NOT_FOUND, translate(UNKNOWN_MATCH_ERROR, null));
            }
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<List<Registration>> loadMatchRegistrations(String matchCode) {
        try {
            net.andresbustamante.yafoot.model.Match match = matchSearchService.findMatchByCode(matchCode);

            if (match != null) {
                return ResponseEntity.ok(registrationMapper.map(match.getRegistrations()));
            } else {
                throw new ResponseStatusException(NOT_FOUND, translate(UNKNOWN_MATCH_ERROR, null));
            }
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
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
                List<Match> result = matchMapper.map(matches);
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        } catch (ApplicationException e) {
            throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), null));
        }
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
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        } catch (ApplicationException e) {
            throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), null));
        }
    }

    @Override
    public ResponseEntity<List<Car>> findCarsForMatch(String matchCode) {
        try {
            net.andresbustamante.yafoot.model.Match match = matchSearchService.findMatchByCode(matchCode);

            if (match != null) {
                List<net.andresbustamante.yafoot.model.Car> cars = carpoolingService.findAvailableCarsByMatch(match);
                return ResponseEntity.ok(carMapper.map(cars));
            } else {
                throw new ResponseStatusException(NOT_FOUND, translate(UNKNOWN_MATCH_ERROR, null));
            }
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }

    @CrossOrigin(exposedHeaders = {HttpHeaders.LOCATION})
    @Override
    public ResponseEntity<Void> registerPlayerToMatch(String matchCode, Registration registration) {
        try {
            UserContext userContext = getUserContext(request);
            net.andresbustamante.yafoot.model.Registration reg = registrationMapper.map(registration);

            net.andresbustamante.yafoot.model.Match match = matchSearchService.findMatchByCode(matchCode);
            Player player = playerSearchService.findPlayerByEmail(reg.getPlayer().getEmail());

            if (match == null) {
                throw new ResponseStatusException(NOT_FOUND, translate(UNKNOWN_MATCH_ERROR, null));
            } else if (player == null) {
                throw new ResponseStatusException(BAD_REQUEST, translate(UNKNOWN_PLAYER_ERROR,
                        new String[]{reg.getPlayer().getEmail()}));
            }

            matchManagementService.registerPlayer(player, match, reg.getCar(), userContext);

            String location = MessageFormat.format(matchPlayerApiPath, match.getCode(), reg.getPlayer().getId());
            return ResponseEntity.created(getLocationURI(location)).build();
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        } catch (ApplicationException e) {
            throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), null));
        }
    }

    @Override
    public ResponseEntity<Void> updateCarForRegistration(String matchCode, Integer playerId, CarConfirmation carConfirmation) {
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
                    throw new ResponseStatusException(NOT_FOUND, translate(UNKNOWN_PLAYER_REGISTRATION_ERROR, null));
                }
            } else {
                throw new ResponseStatusException(NOT_FOUND, translate(UNKNOWN_MATCH_ERROR, null));
            }
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        } catch (UserNotAuthorisedException e) {
            throw new ResponseStatusException(FORBIDDEN, translate(UNAUTHORISED_USER_ERROR, null));
        } catch (ApplicationException e) {
            throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), null));
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
                throw new ResponseStatusException(NOT_FOUND, translate(UNKNOWN_MATCH_ERROR, null));
            } else {
                throw new ResponseStatusException(NOT_FOUND, translate(UNKNOWN_PLAYER_REGISTRATION_ERROR, null));
            }
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        } catch (ApplicationException e) {
            throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), null));
        }
    }
}
