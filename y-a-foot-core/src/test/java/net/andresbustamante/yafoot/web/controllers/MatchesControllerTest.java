package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.model.*;
import net.andresbustamante.yafoot.core.services.CarpoolingService;
import net.andresbustamante.yafoot.core.services.MatchManagementService;
import net.andresbustamante.yafoot.core.services.MatchSearchService;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import net.andresbustamante.yafoot.web.dto.CarConfirmation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.andresbustamante.yafoot.core.model.enums.MatchStatusEnum.CREATED;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchesController.class)
class MatchesControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MatchSearchService matchSearchService;

    @MockBean
    private PlayerSearchService playerSearchService;

    @MockBean
    private MatchManagementService matchManagementService;

    @MockBean
    private CarpoolingService carpoolingService;

    @Value("${matches.api.service.path}")
    private String matchesApiPath;

    @Test
    void loadMatchByExistingCode() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";

        Match match = new Match(1);
        match.setCode(matchCode);

        given(matchSearchService.findMatchByCode(anyString())).willReturn(match);

        // When
        mvc.perform(get("/matches/{0}", matchCode)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/matches/{0}", "/matches/{0}/registrations", "/matches/{0}/cars"})
    void loadMatchByUnknownCode(String path) throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";

        given(matchSearchService.findMatchByCode(anyString())).willReturn(null);

        // When
        mvc.perform(get(path, matchCode)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void loadMatchByCodeWhileDatabaseIsUnavailable() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";

        given(matchSearchService.findMatchByCode(anyString())).willThrow(DatabaseException.class);

        // When
        mvc.perform(get("/matches/{0}", matchCode)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void loadExistingMatchRegistrations() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";

        Match match = new Match(1);
        match.setCode(matchCode);

        Registration registration1 = new Registration(new RegistrationId(1, 1));
        Registration registration2 = new Registration(new RegistrationId(1, 2));
        match.setRegistrations(List.of(registration1, registration2));

        given(matchSearchService.findMatchByCode(anyString())).willReturn(match);

        // When
        mvc.perform(get("/matches/{0}/registrations", matchCode)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void loadMatchRegistrationsWhileDatabaseIsUnavailable() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";

        given(matchSearchService.findMatchByCode(anyString())).willThrow(DatabaseException.class);

        // When
        mvc.perform(get("/matches/{0}/registrations", matchCode)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findPastMatches() throws Exception {
        // Given
        LocalDate today = LocalDate.now();

        Match match1 = new Match(1);
        match1.setDate(today.minusDays(3).atTime(OffsetTime.now()));
        Match match2 = new Match(1);
        match2.setDate(today.minusDays(4).atTime(OffsetTime.now()));

        given(matchSearchService.findMatchesByPlayer(any(Player.class), eq(null), any(LocalDate.class))).willReturn(
                List.of(match1, match2));

        // When
        mvc.perform(get("/matches?endDate={0}", today.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(playerSearchService).findPlayerByEmail(anyString());
    }

    @Test
    void findFutureMatches() throws Exception {
        // Given
        LocalDate today = LocalDate.now();

        Match match1 = new Match(1);
        match1.setDate(today.plusDays(3).atTime(OffsetTime.now()));
        Match match2 = new Match(1);
        match2.setDate(today.plusDays(4).atTime(OffsetTime.now()));

        given(matchSearchService.findMatchesByPlayer(any(Player.class), any(LocalDate.class), eq(null))).willReturn(
                List.of(match1, match2));

        // When
        mvc.perform(get("/matches?startDate={0}", today.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(playerSearchService).findPlayerByEmail(anyString());
    }

    @Test
    void findMatchesBetweenInvalidInterval() throws Exception {
        // Given
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        // When
        mvc.perform(get("/matches?startDate={0}&endDate={1}", tomorrow.format(DateTimeFormatter.ISO_LOCAL_DATE), today.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isBadRequest());

        verify(playerSearchService, never()).findPlayerByEmail(anyString());
    }

    @Test
    void findMatchesNoResults() throws Exception {
        // Given
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        given(matchSearchService.findMatchesByPlayer(any(Player.class), any(LocalDate.class), any(LocalDate.class))).willReturn(null);

        // When
        mvc.perform(get("/matches?startDate={0}&endDate={1}", today.format(DateTimeFormatter.ISO_LOCAL_DATE), tomorrow.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(playerSearchService).findPlayerByEmail(anyString());
    }

    @Test
    void findMatchesWhileDatabaseIsUnavailable() throws Exception {
        // Given
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        given(playerSearchService.findPlayerByEmail(anyString())).willThrow(DatabaseException.class);
        given(matchSearchService.findMatchesByPlayer(any(Player.class), any(LocalDate.class), any(LocalDate.class)))
                .willThrow(DatabaseException.class);

        // When
        mvc.perform(get("/matches?startDate={0}&endDate={1}", today.format(DateTimeFormatter.ISO_LOCAL_DATE), tomorrow.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createMatch() throws Exception {
        // Given
        net.andresbustamante.yafoot.web.dto.Match match = new net.andresbustamante.yafoot.web.dto.Match();
        match.setDate(OffsetDateTime.now().plusDays(3));
        match.setNumPlayersMin(8);

        net.andresbustamante.yafoot.web.dto.Site site = new net.andresbustamante.yafoot.web.dto.Site();
        site.setName("My site");
        site.setAddress("123 Fake Address");
        match.setSite(site);

        Integer id = 1;

        given(matchManagementService.saveMatch(any(Match.class), any(UserContext.class))).willReturn(id);

        // When
        mvc.perform(post("/matches")
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .content(objectMapper.writeValueAsString(match))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isCreated())
                .andExpect(header().exists(LOCATION));
    }

    @Test
    void createMatchInThePast() throws Exception {
        // Given
        net.andresbustamante.yafoot.web.dto.Match match = new net.andresbustamante.yafoot.web.dto.Match();
        match.setDate(OffsetDateTime.now().minusDays(3));
        match.setNumPlayersMin(8);

        net.andresbustamante.yafoot.web.dto.Site site = new net.andresbustamante.yafoot.web.dto.Site();
        site.setName("My site");
        site.setAddress("123 Fake Address");
        match.setSite(site);

        ApplicationException exception = new ApplicationException("match.past.new.date.error", "message");
        given(matchManagementService.saveMatch(any(Match.class), any(UserContext.class))).willThrow(exception);

        // When
        mvc.perform(post("/matches")
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .content(objectMapper.writeValueAsString(match))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMatchWhileDatabaseIsUnavailable() throws Exception {
        // Given
        net.andresbustamante.yafoot.web.dto.Match match = new net.andresbustamante.yafoot.web.dto.Match();
        match.setDate(OffsetDateTime.now().plusDays(3));
        match.setNumPlayersMin(8);

        net.andresbustamante.yafoot.web.dto.Site site = new net.andresbustamante.yafoot.web.dto.Site();
        site.setName("My site");
        site.setAddress("123 Fake Address");
        match.setSite(site);

        given(matchManagementService.saveMatch(any(Match.class), any(UserContext.class))).willThrow(DatabaseException.class);

        // When
        mvc.perform(post("/matches")
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .content(objectMapper.writeValueAsString(match))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findCarsForExistingMatch() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";

        Match match = new Match(1);
        match.setCode(matchCode);

        Car car1 = new Car(1);
        Car car2 = new Car(2);

        given(matchSearchService.findMatchByCode(anyString())).willReturn(match);
        given(carpoolingService.findAvailableCarsByMatch(any(Match.class))).willReturn(List.of(car1, car2));

        // When
        mvc.perform(get("/matches/{0}/cars", matchCode)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findCarsForExistingMatchEmptyResult() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";

        Match match = new Match(1);
        match.setCode(matchCode);

        given(matchSearchService.findMatchByCode(anyString())).willReturn(match);
        given(carpoolingService.findAvailableCarsByMatch(any(Match.class))).willReturn(Collections.emptyList());

        // When
        mvc.perform(get("/matches/{0}/cars", matchCode)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findCarsWhileDatabaseIsUnavailable() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";

        given(matchSearchService.findMatchByCode(anyString())).willThrow(DatabaseException.class);
        given(carpoolingService.findAvailableCarsByMatch(any(Match.class))).willThrow(DatabaseException.class);

        // When
        mvc.perform(get("/matches/{0}/cars", matchCode)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void registerPlayerToExistingMatch() throws Exception {
        // Given
        String email = VALID_EMAIL;
        String matchCode = "ABCDEFGHIJ";
        Match match = new Match(1);
        match.setCode(matchCode);
        match.setDate(OffsetDateTime.now().plusDays(3));
        match.setNumPlayersMin(8);

        Player player = new Player(2, "DOE", "John", email, null);
        net.andresbustamante.yafoot.web.dto.Player playerDto = new net.andresbustamante.yafoot.web.dto.Player();
        playerDto.setId(2);
        playerDto.setEmail(email);

        net.andresbustamante.yafoot.web.dto.Registration registration = new net.andresbustamante.yafoot.web.dto.Registration();
        registration.setPlayer(playerDto);
        registration.setCarConfirmed(false);

        given(matchSearchService.findMatchByCode(anyString())).willReturn(match);
        given(playerSearchService.findPlayerByEmail(anyString())).willReturn(player);

        // When
        mvc.perform(post("/matches/{0}/registrations", matchCode)
                .header(AUTHORIZATION, getAuthString(email))
                .content(objectMapper.writeValueAsString(registration))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isCreated())
                .andExpect(header().exists(LOCATION))
                .andExpect(header().string(LOCATION, apiPublicUrl + matchesApiPath + "/" + match.getCode() + "/registrations/" + player.getId()));
    }

    @Test
    void registerPlayerToUnknownMatch() throws Exception {
        // Given
        String email = VALID_EMAIL;
        String matchCode = "ABCDEFGHIJ";

        Player player = new Player(2, "DOE", "John", email, null);
        net.andresbustamante.yafoot.web.dto.Player playerDto = new net.andresbustamante.yafoot.web.dto.Player();
        playerDto.setId(2);
        playerDto.setEmail(email);

        net.andresbustamante.yafoot.web.dto.Registration registration = new net.andresbustamante.yafoot.web.dto.Registration();
        registration.setPlayer(playerDto);
        registration.setCarConfirmed(false);

        given(matchSearchService.findMatchByCode(anyString())).willReturn(null);
        given(playerSearchService.findPlayerByEmail(anyString())).willReturn(player);

        // When
        mvc.perform(post("/matches/{0}/registrations", matchCode)
                .header(AUTHORIZATION, getAuthString(email))
                .content(objectMapper.writeValueAsString(registration))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void registerUnknownPlayerToExistingMatch() throws Exception {
        // Given
        String email = "doe.john@email.com";
        String matchCode = "ABCDEFGHIJ";
        Match match = new Match(1);
        match.setCode(matchCode);
        match.setDate(OffsetDateTime.now().plusDays(3));
        match.setNumPlayersMin(8);

        net.andresbustamante.yafoot.web.dto.Player playerDto = new net.andresbustamante.yafoot.web.dto.Player();
        playerDto.setId(2);
        playerDto.setEmail(email);

        net.andresbustamante.yafoot.web.dto.Registration registration = new net.andresbustamante.yafoot.web.dto.Registration();
        registration.setPlayer(playerDto);
        registration.setCarConfirmed(false);

        given(matchSearchService.findMatchByCode(anyString())).willReturn(match);
        given(playerSearchService.findPlayerByEmail(anyString())).willReturn(null);

        // When
        mvc.perform(post("/matches/{0}/registrations", matchCode)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .content(objectMapper.writeValueAsString(registration))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCarForExistingRegistration() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";
        Match match = new Match(1);
        match.setCode(matchCode);
        match.setDate(OffsetDateTime.now().plusDays(3));
        match.setNumPlayersMin(8);

        Player player = new Player(2, VALID_EMAIL, "DOE", "John", null);

        net.andresbustamante.yafoot.web.dto.Car car = new net.andresbustamante.yafoot.web.dto.Car()
                .id(3)
                .numSeats(2)
                .name("Car 3");

        Registration registration = new Registration(new RegistrationId(match.getId(), player.getId()));
        registration.setPlayer(player);
        registration.setCarConfirmed(false);
        match.setRegistrations(List.of(registration));

        CarConfirmation confirmation = new CarConfirmation().car(car).confirmed(true);

        given(matchSearchService.findMatchByCode(anyString())).willReturn(match);
        given(playerSearchService.findPlayerById(anyInt())).willReturn(player);

        // When
        mvc.perform(patch("/matches/{0}/registrations/{1}", matchCode, player.getId())
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .content(objectMapper.writeValueAsString(confirmation))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isAccepted());
    }

    @Test
    void updateCarForUnknownMatch() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";

        Player player = new Player(2, VALID_EMAIL, "DOE", "John", null);

        net.andresbustamante.yafoot.web.dto.Car car = new net.andresbustamante.yafoot.web.dto.Car()
                .id(3)
                .numSeats(2)
                .name("Car 3");

        CarConfirmation confirmation = new CarConfirmation().car(car).confirmed(true);

        given(matchSearchService.findMatchByCode(anyString())).willReturn(null);
        given(playerSearchService.findPlayerById(anyInt())).willReturn(player);

        // When
        mvc.perform(patch("/matches/{0}/registrations/{1}", matchCode, player.getId())
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .content(objectMapper.writeValueAsString(confirmation))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCarForUnknownRegistration() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";
        Match match = new Match(1);
        match.setCode(matchCode);
        match.setDate(OffsetDateTime.now().plusDays(3));
        match.setNumPlayersMin(8);
        match.setRegistrations(new ArrayList<>());

        Player player = new Player(2, VALID_EMAIL, "DOE", "John", null);

        net.andresbustamante.yafoot.web.dto.Car car = new net.andresbustamante.yafoot.web.dto.Car()
                .id(3)
                .numSeats(2)
                .name("Car 3");

        CarConfirmation confirmation = new CarConfirmation().car(car).confirmed(true);

        given(matchSearchService.findMatchByCode(anyString())).willReturn(match);
        given(playerSearchService.findPlayerById(anyInt())).willReturn(player);

        // When
        mvc.perform(patch("/matches/{0}/registrations/{1}", matchCode, player.getId())
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .content(objectMapper.writeValueAsString(confirmation))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCarForUnknownPlayer() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";
        Match match = new Match(1);
        match.setCode(matchCode);
        match.setDate(OffsetDateTime.now().plusDays(3));
        match.setNumPlayersMin(8);

        net.andresbustamante.yafoot.web.dto.Car car = new net.andresbustamante.yafoot.web.dto.Car()
                .id(3)
                .numSeats(2)
                .name("Car 3");

        CarConfirmation confirmation = new CarConfirmation().car(car).confirmed(true);

        given(matchSearchService.findMatchByCode(anyString())).willReturn(match);
        given(playerSearchService.findPlayerById(anyInt())).willReturn(null);

        // When
        mvc.perform(patch("/matches/{0}/registrations/{1}", matchCode, 2)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .content(objectMapper.writeValueAsString(confirmation))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCarForRegistrationWhileDatabaseIsUnavailable() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";

        net.andresbustamante.yafoot.web.dto.Car car = new net.andresbustamante.yafoot.web.dto.Car()
                .id(3)
                .numSeats(2)
                .name("Car 3");

        CarConfirmation confirmation = new CarConfirmation().car(car).confirmed(true);

        given(matchSearchService.findMatchByCode(anyString())).willThrow(DatabaseException.class);
        given(playerSearchService.findPlayerById(anyInt())).willThrow(DatabaseException.class);

        // When
        mvc.perform(patch("/matches/{0}/registrations/{1}", matchCode, 2)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .content(objectMapper.writeValueAsString(confirmation))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void unregisterPlayerFromExistingMatch() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";
        Match match = new Match(1);
        match.setCode(matchCode);
        match.setDate(OffsetDateTime.now().plusDays(3));
        match.setNumPlayersMin(8);

        Player player = new Player(2, VALID_EMAIL, "DOE", "John", null);

        Registration registration = new Registration(new RegistrationId(match.getId(), player.getId()));
        registration.setPlayer(player);
        registration.setCarConfirmed(false);
        match.setRegistrations(List.of(registration));

        given(matchSearchService.findMatchByCode(anyString())).willReturn(match);
        given(playerSearchService.findPlayerById(anyInt())).willReturn(player);

        // When
        mvc.perform(delete("/matches/{0}/registrations/{1}", matchCode, player.getId())
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNoContent());
    }

    @Test
    void unregisterPlayerFromUnknownMatch() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";
        Match match = new Match(1);

        Player player = new Player(2, VALID_EMAIL, "DOE", "John", null);

        Registration registration = new Registration(new RegistrationId(match.getId(), player.getId()));
        registration.setPlayer(player);
        registration.setCarConfirmed(false);

        given(matchSearchService.findMatchByCode(anyString())).willReturn(null);

        // When
        mvc.perform(delete("/matches/{0}/registrations/{1}", matchCode, player.getId())
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void unregisterUnknownPlayerFromMatch() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";
        Match match = new Match(1);
        match.setCode(matchCode);
        match.setDate(OffsetDateTime.now().plusDays(3));
        match.setNumPlayersMin(8);

        given(matchSearchService.findMatchByCode(anyString())).willReturn(match);
        given(playerSearchService.findPlayerById(anyInt())).willReturn(null);

        // When
        mvc.perform(delete("/matches/{0}/registrations/{1}", matchCode, 2)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void unregisterPlayerFromMatchWhileDatabaseIsUnavailable() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";

        given(matchSearchService.findMatchByCode(anyString())).willThrow(DatabaseException.class);
        given(playerSearchService.findPlayerById(anyInt())).willThrow(DatabaseException.class);

        // When
        mvc.perform(delete("/matches/{0}/registrations/{1}", matchCode, 2)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void cancelValidMatch() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";
        Match match = new Match(1);
        match.setCode(matchCode);
        match.setStatus(CREATED);
        match.setDate(OffsetDateTime.now().plusDays(3));
        match.setNumPlayersMin(8);

        given(matchSearchService.findMatchByCode(anyString())).willReturn(match);

        // When
        mvc.perform(delete("/matches/{0}", matchCode)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNoContent());
    }

    @Test
    void cancelUnknownMatch() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";

        given(matchSearchService.findMatchByCode(anyString())).willReturn(null);

        // When
        mvc.perform(delete("/matches/{0}", matchCode)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void cancelMatchWhenDatabaseIsUnavailable() throws Exception {
        // Given
        String matchCode = "ABCDEFGHIJ";

        given(matchSearchService.findMatchByCode(anyString())).willThrow(DatabaseException.class);

        // When
        mvc.perform(delete("/matches/{0}", matchCode)
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }
}