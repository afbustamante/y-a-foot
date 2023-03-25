package net.andresbustamante.yafoot.core.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.web.controllers.AbstractController;
import net.andresbustamante.yafoot.core.exceptions.UnauthorisedUserException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.model.enums.MatchStatusEnum;
import net.andresbustamante.yafoot.core.model.enums.SportEnum;
import net.andresbustamante.yafoot.core.services.CarpoolingService;
import net.andresbustamante.yafoot.core.services.MatchManagementService;
import net.andresbustamante.yafoot.core.services.MatchSearchService;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import net.andresbustamante.yafoot.core.web.mappers.CarMapper;
import net.andresbustamante.yafoot.core.web.mappers.MatchMapper;
import net.andresbustamante.yafoot.core.web.mappers.RegistrationMapper;
import net.andresbustamante.yafoot.web.dto.Car;
import net.andresbustamante.yafoot.web.dto.CarConfirmation;
import net.andresbustamante.yafoot.web.dto.Match;
import net.andresbustamante.yafoot.web.dto.MatchForm;
import net.andresbustamante.yafoot.web.dto.MatchStatus;
import net.andresbustamante.yafoot.web.dto.Registration;
import net.andresbustamante.yafoot.web.dto.RegistrationForm;
import net.andresbustamante.yafoot.web.dto.SportCode;
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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpStatus.*;

/**
 * REST service for managing and searching matches.
 *
 * @author andresbustamante
 */
@RestController
public class MatchesController extends AbstractController implements MatchesApi {

    /* Message codes */
    private static final String UNKNOWN_MATCH_ERROR = "unknown.match.error";
    private static final String UNKNOWN_PLAYER_REGISTRATION_ERROR = "unknown.player.registration.error";
    private static final String UNKNOWN_PLAYER_ERROR = "unknown.player.error";

    private final MatchSearchService matchSearchService;
    private final PlayerSearchService playerSearchService;
    private final MatchManagementService matchManagementService;
    private final CarpoolingService carpoolingService;
    private final MatchMapper matchMapper;
    private final RegistrationMapper registrationMapper;
    private final CarMapper carMapper;

    @Value("${api.matches.one.path}")
    private String matchApiPath;

    @Value("${api.matches.one.registrations.one.path}")
    private String matchRegistrationApiPath;

    @Autowired
    public MatchesController(MatchSearchService matchSearchService, PlayerSearchService playerSearchService,
                             CarpoolingService carpoolingService,
                             MatchManagementService matchManagementService,
                             MatchMapper matchMapper, RegistrationMapper registrationMapper, CarMapper carMapper,
                             HttpServletRequest request, ObjectMapper objectMapper,
                             ApplicationContext applicationContext) {
        super(request, objectMapper, applicationContext);
        this.matchSearchService = matchSearchService;
        this.matchManagementService = matchManagementService;
        this.playerSearchService = playerSearchService;
        this.carpoolingService = carpoolingService;
        this.matchMapper = matchMapper;
        this.registrationMapper = registrationMapper;
        this.carMapper = carMapper;
    }

    @Override
    public ResponseEntity<Match> loadMatchByCode(String code) {
        try {
            net.andresbustamante.yafoot.core.model.Match match = matchSearchService.findMatchByCode(code);

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
    public ResponseEntity<List<Registration>> loadMatchRegistrations(String code) {
        try {
            net.andresbustamante.yafoot.core.model.Match match = matchSearchService.findMatchByCode(code);

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
                                                   @DateTimeFormat(iso = DATE) LocalDate endDate,
                                                   SportCode sport,
                                                   MatchStatus status) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            UserContext ctx = getUserContext();

            Player player = playerSearchService.findPlayerByEmail(ctx.getUsername(), ctx);

            List<net.andresbustamante.yafoot.core.model.Match> matches = matchSearchService.findMatchesByPlayer(player,
                    (status != null) ? MatchStatusEnum.valueOf(status.name()) : null,
                    (sport != null) ? SportEnum.valueOf(sport.name()) : null,
                    startDate, endDate);

            if (CollectionUtils.isNotEmpty(matches)) {
                List<Match> result = matchMapper.map(matches);
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        } catch (ApplicationException e) {
            throw new ResponseStatusException(FORBIDDEN, translate(e.getCode(), null));
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<Void> cancelMatch(String code) {
        try {
            UserContext userContext = getUserContext();

            net.andresbustamante.yafoot.core.model.Match match = matchSearchService.findMatchByCode(code);

            if (match != null) {
                matchManagementService.cancelMatch(match, userContext);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        } catch (ApplicationException e) {
            throw new ResponseStatusException(FORBIDDEN, translate(e.getCode(), null));
        }
    }

    @CrossOrigin(exposedHeaders = {HttpHeaders.LOCATION})
    @Override
    public ResponseEntity<Void> createMatch(@Valid MatchForm match) {
        try {
            UserContext userContext = getUserContext();
            net.andresbustamante.yafoot.core.model.Match m = matchMapper.map(match);
            matchManagementService.saveMatch(m, userContext);

            String location = String.format(matchApiPath, m.getCode());
            return ResponseEntity.created(getLocationURI(location)).build();
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        } catch (ApplicationException e) {
            throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), null));
        }
    }

    @Override
    public ResponseEntity<List<Car>> findCarsForMatch(String code) {
        try {
            net.andresbustamante.yafoot.core.model.Match match = matchSearchService.findMatchByCode(code);

            if (match != null) {
                List<net.andresbustamante.yafoot.core.model.Car> cars = carpoolingService.findAvailableCarsByMatch(
                        match);
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
    public ResponseEntity<Void> registerPlayerToMatch(String code, RegistrationForm registration) {
        try {
            UserContext ctx = getUserContext();

            net.andresbustamante.yafoot.core.model.Match match = matchSearchService.findMatchByCode(code);
            Player player = registration.getPlayerId() != null
                    ? playerSearchService.findPlayerById(registration.getPlayerId())    // A given player
                    : playerSearchService.findPlayerByEmail(ctx.getUsername(), ctx); // The player himself/herself

            if (match == null) {
                throw new ResponseStatusException(NOT_FOUND, translate(UNKNOWN_MATCH_ERROR, null));
            } else if (player == null) {
                throw new ResponseStatusException(BAD_REQUEST, translate(UNKNOWN_PLAYER_ERROR,
                        new String[]{registration.getPlayerId().toString()}));
            }

            net.andresbustamante.yafoot.core.model.Car car = null;

            if (registration.getCarId() != null) {
                car = new net.andresbustamante.yafoot.core.model.Car(registration.getCarId());
            }

            matchManagementService.registerPlayer(player, match, car, ctx);

            String location = String.format(matchRegistrationApiPath, match.getCode(),
                    registration.getPlayerId());
            return ResponseEntity.created(getLocationURI(location)).build();
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        } catch (ApplicationException e) {
            throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), null));
        }
    }

    @Override
    public ResponseEntity<Void> updateCarForRegistration(String code, Integer pid,
                                                         CarConfirmation carConfirmation) {
        try {
            net.andresbustamante.yafoot.core.model.Match match = matchSearchService.findMatchByCode(code);

            if (match != null && match.getRegistrations() != null) {
                Player player = playerSearchService.findPlayerById(pid);

                if (player == null) {
                    throw new ResponseStatusException(NOT_FOUND, translate(UNKNOWN_PLAYER_REGISTRATION_ERROR, null));
                }

                Optional<net.andresbustamante.yafoot.core.model.Registration> registration =
                        match.getRegistrations().stream().filter(reg -> reg.getPlayer().equals(player)).findFirst();

                if (registration.isPresent()) {
                    UserContext ctx = getUserContext();

                    net.andresbustamante.yafoot.core.model.Car car = new net.andresbustamante.yafoot.core.model.Car(
                            carConfirmation.getCarId());

                    carpoolingService.updateCarpoolingInformation(match, player, car, carConfirmation.isConfirmed(),
                            ctx);
                    return ResponseEntity.accepted().build();
                } else {
                    throw new ResponseStatusException(NOT_FOUND, translate(UNKNOWN_PLAYER_REGISTRATION_ERROR, null));
                }
            } else {
                throw new ResponseStatusException(NOT_FOUND, translate(UNKNOWN_MATCH_ERROR, null));
            }
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        } catch (UnauthorisedUserException e) {
            throw new ResponseStatusException(FORBIDDEN, translate(UNAUTHORISED_USER_ERROR, null));
        } catch (ApplicationException e) {
            throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), null));
        }
    }

    @Override
    public ResponseEntity<Void> unregisterPlayerFromMatch(String code, Integer pid) {
        try {
            UserContext userContext = getUserContext();

            net.andresbustamante.yafoot.core.model.Match match = matchSearchService.findMatchByCode(code);
            Player player = playerSearchService.findPlayerById(pid);

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
