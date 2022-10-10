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
import net.andresbustamante.yafoot.web.dto.*;
import net.andresbustamante.yafoot.core.web.mappers.CarMapper;
import net.andresbustamante.yafoot.core.web.mappers.MatchMapper;
import net.andresbustamante.yafoot.core.web.mappers.RegistrationMapper;
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
import java.util.Optional;

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
                             HttpServletRequest request, ObjectMapper objectMapper, ApplicationContext applicationContext) {
        super(request, objectMapper, applicationContext);
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
            net.andresbustamante.yafoot.core.model.Match match = matchSearchService.findMatchByCode(matchCode);

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
            net.andresbustamante.yafoot.core.model.Match match = matchSearchService.findMatchByCode(matchCode);

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
            UserContext ctx = getUserContext(request);

            Player player = playerSearchService.findPlayerByEmail(ctx.getUsername());

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
        } catch (DatabaseException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DATABASE_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<Void> cancelMatch(String matchCode) {
        try {
            UserContext userContext = getUserContext(request);

            net.andresbustamante.yafoot.core.model.Match match = matchSearchService.findMatchByCode(matchCode);

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
    public ResponseEntity<Void> createMatch(@Valid Match match) {
        try {
            UserContext userContext = getUserContext(request);
            net.andresbustamante.yafoot.core.model.Match m = matchMapper.map(match);
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
            net.andresbustamante.yafoot.core.model.Match match = matchSearchService.findMatchByCode(matchCode);

            if (match != null) {
                List<net.andresbustamante.yafoot.core.model.Car> cars = carpoolingService.findAvailableCarsByMatch(match);
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
            net.andresbustamante.yafoot.core.model.Registration reg = registrationMapper.map(registration);

            net.andresbustamante.yafoot.core.model.Match match = matchSearchService.findMatchByCode(matchCode);
            Player player = playerSearchService.findPlayerByEmail(reg.getPlayer().getEmail());

            if (match == null) {
                throw new ResponseStatusException(NOT_FOUND, translate(UNKNOWN_MATCH_ERROR, null));
            } else if (player == null) {
                throw new ResponseStatusException(BAD_REQUEST, translate(UNKNOWN_PLAYER_ERROR,
                        new String[]{reg.getPlayer().getEmail()}));
            }

            matchManagementService.registerPlayer(player, match, reg.getCar(), userContext);

            String location = MessageFormat.format(matchRegistrationApiPath, match.getCode(), reg.getPlayer().getId());
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
            net.andresbustamante.yafoot.core.model.Match match = matchSearchService.findMatchByCode(matchCode);

            if (match != null && match.getRegistrations() != null) {
                Player player = playerSearchService.findPlayerById(playerId);

                if (player == null) {
                    throw new ResponseStatusException(NOT_FOUND, translate(UNKNOWN_PLAYER_REGISTRATION_ERROR, null));
                }

                Optional<net.andresbustamante.yafoot.core.model.Registration> registration = match.getRegistrations().stream().filter(
                        reg -> reg.getPlayer().equals(player)).findFirst();

                if (registration.isPresent()) {
                    UserContext ctx = getUserContext(request);

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
        } catch (UnauthorisedUserException e) {
            throw new ResponseStatusException(FORBIDDEN, translate(UNAUTHORISED_USER_ERROR, null));
        } catch (ApplicationException e) {
            throw new ResponseStatusException(BAD_REQUEST, translate(e.getCode(), null));
        }
    }

    @Override
    public ResponseEntity<Void> unregisterPlayerFromMatch(String matchCode, Integer playerId) {
        try {
            UserContext userContext = getUserContext(request);

            net.andresbustamante.yafoot.core.model.Match match = matchSearchService.findMatchByCode(matchCode);
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
