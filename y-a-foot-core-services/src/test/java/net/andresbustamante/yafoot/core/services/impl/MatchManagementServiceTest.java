package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.commons.services.AbstractServiceUnitTest;
import net.andresbustamante.yafoot.core.dao.CarDao;
import net.andresbustamante.yafoot.core.dao.MatchDao;
import net.andresbustamante.yafoot.core.dao.PlayerDao;
import net.andresbustamante.yafoot.core.dao.SiteDao;
import net.andresbustamante.yafoot.core.events.MatchPlayerUnsubscriptionEvent;
import net.andresbustamante.yafoot.core.exceptions.PastMatchException;
import net.andresbustamante.yafoot.core.exceptions.UnauthorisedUserException;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Registration;
import net.andresbustamante.yafoot.core.model.RegistrationId;
import net.andresbustamante.yafoot.core.model.Site;
import net.andresbustamante.yafoot.core.services.CarManagementService;
import net.andresbustamante.yafoot.core.services.CarpoolingService;
import net.andresbustamante.yafoot.core.services.SiteManagementService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static net.andresbustamante.yafoot.core.model.enums.MatchStatusEnum.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchManagementServiceTest extends AbstractServiceUnitTest {

    @InjectMocks
    private MatchManagementServiceImpl matchManagementService;

    @Mock
    private MatchDao matchDAO;

    @Mock
    private SiteDao siteDAO;

    @Mock
    private CarDao carDAO;

    @Mock
    private SiteManagementService siteManagementService;

    @Mock
    private CarManagementService carManagementService;

    @Mock
    private PlayerDao playerDAO;

    @Mock
    private CarpoolingService carpoolingService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    void saveMatchUsingExistingSite() throws Exception {
        // Given
        final Player player = new Player(1);
        final Site site = new Site(1);
        final Match match = new Match(1);
        match.setDate(LocalDateTime.now().plusDays(1L));
        match.setSite(site);
        final UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(matchDAO.isCodeAlreadyRegistered(anyString())).thenReturn(false);
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);
        when(siteDAO.findSiteById(anyInt())).thenReturn(site);
        matchManagementService.saveMatch(match, ctx);

        // Then
        verify(matchDAO).isCodeAlreadyRegistered(anyString());
        verify(siteDAO).findSiteById(any());
        verify(siteManagementService, never()).saveSite(any(Site.class), any(UserContext.class));

        assertNotNull(match.getCode());
        assertNotNull(match.getCreator());
        assertEquals(player, match.getCreator());
    }

    @Test
    void saveMatchUsingNewSite() throws Exception {
        // Given
        final Player player = new Player(1);
        final Site site = new Site();
        final Match match = new Match(1);
        match.setDate(LocalDateTime.now().plusDays(1));
        match.setSite(site);
        final UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(matchDAO.isCodeAlreadyRegistered(anyString())).thenReturn(false);
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);
        matchManagementService.saveMatch(match, ctx);

        // Then
        verify(matchDAO).isCodeAlreadyRegistered(anyString());
        verify(siteDAO, never()).findSiteById(any());
        verify(siteManagementService).saveSite(any(Site.class), any(UserContext.class));

        assertNotNull(match.getCode());
        assertNotNull(match.getCreator());
        assertEquals(player, match.getCreator());
    }

    @Test
    void saveMatchUsingUnknownSite() throws Exception {
        // Given
        final Player player = new Player(1);
        final Site site = new Site(100);
        final Match match = new Match(1);
        match.setDate(LocalDateTime.now().plusDays(1));
        match.setSite(site);
        final UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(matchDAO.isCodeAlreadyRegistered(anyString())).thenReturn(false);
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);
        when(siteDAO.findSiteById(anyInt())).thenReturn(null);
        assertThrows(DatabaseException.class, () -> matchManagementService.saveMatch(match, ctx));

        // Then
        verify(matchDAO).isCodeAlreadyRegistered(anyString());
        verify(siteDAO).findSiteById(any());
        verify(matchDAO, never()).saveMatch(any());
        verify(siteManagementService, never()).saveSite(any(Site.class), any(UserContext.class));
    }

    @Test
    void registerPlayerWithNoCar() throws Exception {
        // Given
        final Player player = new Player(1);
        final Match match = new Match(1);
        match.setDate(LocalDateTime.now().plusDays(1L));
        match.setRegistrations(Collections.emptyList());
        final UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        matchManagementService.registerPlayer(player, match, null, ctx);

        // Then
        verify(carDAO, never()).findCarById(anyInt());
        verify(carManagementService, never()).saveCar(any(Car.class), any(UserContext.class));
        verify(matchDAO).registerPlayer(any(Player.class), any(Match.class), eq(null), eq(null));
    }

    @Test
    void registerPlayerWithExistingCar() throws Exception {
        // Given
        final Player player = new Player(1);
        final Match match = new Match(1);
        match.setDate(LocalDateTime.now().plusDays(1L));
        match.setRegistrations(Collections.emptyList());
        final Car car = new Car(1);
        final UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(car);
        matchManagementService.registerPlayer(player, match, car, ctx);

        // Then
        verify(carDAO).findCarById(anyInt());
        verify(carManagementService, never()).saveCar(any(Car.class), any(UserContext.class));
        verify(matchDAO).registerPlayer(any(Player.class), any(Match.class), any(Car.class), eq(false));
    }

    @Test
    void registerPlayerWithUnknownCar() throws Exception {
        // Given
        final Player player = new Player(1);
        final Match match = new Match(1);
        match.setDate(LocalDateTime.now().plusDays(1L));
        match.setRegistrations(Collections.emptyList());
        final Car car = new Car(100);
        final UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(null);
        assertThrows(DatabaseException.class, () -> matchManagementService.registerPlayer(player, match, car, ctx));

        // Then
        verify(carDAO).findCarById(anyInt());
        verify(carManagementService, never()).saveCar(any(Car.class), any(UserContext.class));
        verify(matchDAO, never()).registerPlayer(any(Player.class), any(Match.class), any(Car.class), anyBoolean());
    }

    @Test
    void registerPlayerWithSomebodyElsesCar() throws Exception {
        // Given
        final Player player1 = new Player(1);
        player1.setEmail("test@email.com");
        final Player player2 = new Player(2);
        player2.setEmail("another@email.com");
        final Match match = new Match(1);
        match.setDate(LocalDateTime.now().plusDays(1L));
        match.setCarpoolingEnabled(true);
        match.setRegistrations(Collections.emptyList());
        final Car car = new Car(100);
        car.setDriver(player2);
        final UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(car);
        matchManagementService.registerPlayer(player1, match, car, ctx);

        // Then
        verify(carDAO).findCarById(anyInt());
        verify(carManagementService, never()).saveCar(any(Car.class), any(UserContext.class));
        verify(matchDAO).registerPlayer(any(Player.class), any(Match.class), any(Car.class), eq(false));
        verify(carpoolingService).processCarSeatRequest(any(Match.class), any(Player.class), any(Car.class),
                any(UserContext.class));
    }

    @Test
    void registerPlayerWithNewCar() throws Exception {
        // Given
        final Player player = new Player(1);
        final Match match = new Match(1);
        match.setDate(LocalDateTime.now().plusDays(1L));
        match.setRegistrations(Collections.emptyList());
        final Car car = new Car();
        final UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        matchManagementService.registerPlayer(player, match, car, ctx);

        // Then
        verify(carDAO, never()).findCarById(anyInt());
        verify(carManagementService).saveCar(any(Car.class), any(UserContext.class));
        verify(matchDAO).registerPlayer(any(Player.class), any(Match.class), any(Car.class), anyBoolean());
    }

    @Test
    void registerPlayerWhenAlreadyRegistered() {
        // Given
        final Player player1 = new Player(1);
        player1.setEmail("player@email.com");
        final Match match = new Match(1);
        match.setDate(LocalDateTime.now().plusDays(1L));
        final Registration registration1 = new Registration(new RegistrationId(match.getId(), player1.getId()));
        registration1.setPlayer(player1);
        match.setRegistrations(List.of(registration1));
        final UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        assertDoesNotThrow(() -> matchManagementService.registerPlayer(player1, match, null, ctx));

        // Then
        verify(matchDAO).unregisterPlayer(any(Player.class), any(Match.class));
        verify(matchDAO).registerPlayer(any(Player.class), any(Match.class), eq(null), eq(null));
    }

    @Test
    void registerPlayerWhenAlreadyRegisteredWithPassengers() throws Exception {
        // Given
        final Player player1 = new Player(1);
        player1.setEmail("player@email.com");
        final Player player2 = new Player(2);
        player2.setEmail("player2@email.com");
        final Car car1 = new Car(1);
        car1.setDriver(player1);
        final Match match = new Match(1);
        match.setCarpoolingEnabled(true);
        match.setDate(LocalDateTime.now().plusDays(1L));
        final Registration registration1 = new Registration(new RegistrationId(match.getId(), player1.getId()));
        registration1.setPlayer(player1);
        registration1.setCar(car1);
        registration1.setCarConfirmed(true);
        final Registration registration2 = new Registration(new RegistrationId(match.getId(), player2.getId()));
        registration2.setPlayer(player2);
        registration2.setCar(car1);
        registration2.setCarConfirmed(true);
        match.setRegistrations(List.of(registration1, registration2));
        final UserContext ctx = new UserContext();
        ctx.setUsername("player@email.com");

        // When
        when(matchDAO.loadRegistration(any(Match.class), any(Player.class))).thenReturn(registration1);
        when(carDAO.findCarById(anyInt())).thenReturn(car1);
        assertDoesNotThrow(() -> matchManagementService.registerPlayer(player1, match, car1, ctx));

        // Then
        verify(carpoolingService).processTransportationChange(any(Match.class), any(Car.class), any(Car.class),
                any(UserContext.class));
        verify(matchDAO).unregisterPlayer(any(Player.class), any(Match.class));
        verify(matchDAO).registerPlayer(any(Player.class), any(Match.class), any(Car.class), anyBoolean());
    }

    @Test
    void registerPlayerWhenFullMatch() {
        // Given
        final Player player = new Player(1);
        final Match match = new Match(1);
        match.setDate(LocalDateTime.now().plusDays(1L));
        match.setNumPlayersMax(2);
        final Registration registration1 = new Registration();
        registration1.setPlayer(new Player(2, "Two", "Player", "player.two@email.com", ""));
        final Registration registration2 = new Registration();
        registration2.setPlayer(new Player(3, "Three", "Player", "player.three@email.com", ""));
        match.setRegistrations(List.of(registration1, registration2));
        final UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        assertThrows(ApplicationException.class, () -> matchManagementService.registerPlayer(player, match, null, ctx));

        // Then
        verify(matchDAO, never()).registerPlayer(any(Player.class), any(Match.class), any(Car.class), anyBoolean());
    }

    @Test
    void unregisterPlayer() throws Exception {
        // Given
        final Player player = new Player(1);
        player.setEmail("test@email.com");
        final Match match = new Match(1);
        match.setCreator(player);
        final Registration registration = new Registration(new RegistrationId(1, 1));
        registration.setPlayer(player);
        match.setRegistrations(List.of(registration));
        match.setCode("code");
        final UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // Then
        matchManagementService.unregisterPlayer(player, match, ctx);

        // When
        verify(matchDAO).unregisterPlayer(any(), any());
    }

    @Test
    void unregisterPlayerWhenNotRegistered() {
        // Given
        final Player player = new Player(1);
        final Match match = new Match(1);
        match.setCreator(new Player(2));
        match.setRegistrations(Collections.emptyList());
        match.setCode("code");
        final UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        assertThrows(ApplicationException.class, () -> matchManagementService.unregisterPlayer(player, match, ctx));

        // Then
        verify(matchDAO, never()).unregisterPlayer(any(), any());
    }

    @Test
    void unregisterPlayerWithAlert() throws Exception {
        // Given
        ReflectionTestUtils.setField(matchManagementService, "matchPlayerUnregistrationsQueue",
                "test-queue");

        final Player player = new Player(1);
        player.setEmail("test@email.com");
        player.setFirstName("Luck");
        player.setPreferredLanguage(Locale.CHINESE.getLanguage());

        final Match match = new Match(1);
        match.setCreator(player);
        match.setNumPlayersMin(1);
        match.setDate(LocalDateTime.now().plusDays(7));

        final Registration registration = new Registration(new RegistrationId(1, 1));
        registration.setPlayer(player);
        match.setRegistrations(List.of(registration));
        match.setCode("code");
        final UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // Then
        matchManagementService.unregisterPlayer(player, match, ctx);

        // When
        verify(matchDAO).unregisterPlayer(any(), any());
        verify(rabbitTemplate).convertAndSend(anyString(), any(MatchPlayerUnsubscriptionEvent.class));
    }

    @Test
    void unregisterPlayerWithCarpoolRequests() throws Exception {
        // Given
        final Player player = new Player(1);
        player.setEmail("test@email.com");
        final Match match = new Match(1);
        match.setCode("code");
        match.setCreator(player);
        match.setCarpoolingEnabled(true);
        final Car car = new Car(1);
        car.setDriver(player);
        final Registration registration1 = new Registration(new RegistrationId(match.getId(), player.getId()));
        registration1.setPlayer(player);
        registration1.setCar(car);
        registration1.setCarConfirmed(true);

        final Player anotherPlayer = new Player(2);
        anotherPlayer.setEmail("another.player@email.com");
        final Registration registration2 = new Registration(new RegistrationId(match.getId(), anotherPlayer.getId()));
        registration2.setPlayer(anotherPlayer);
        registration2.setCar(car);
        registration2.setCarConfirmed(true);

        match.setRegistrations(List.of(registration1, registration2));

        final UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // Then
        when(carpoolingService.findAvailableCarsByMatch(any(Match.class))).thenReturn(List.of(car));
        when(matchDAO.findPassengerRegistrationsByCar(any(Match.class), any(Car.class)))
                .thenReturn(List.of(registration2));

        matchManagementService.unregisterPlayer(player, match, ctx);

        // When
        verify(matchDAO).unregisterPlayer(any(Player.class), any(Match.class));
        verify(carpoolingService).findAvailableCarsByMatch(any(Match.class));
        verify(carpoolingService).updateCarpoolingInformation(any(Match.class), any(Player.class), any(Car.class),
                eq(false), any(UserContext.class));
    }

    @Test
    void unregisterPlayerFromAllMatches() throws Exception {
        // Given
        final Player player = new Player(1);

        // When
        matchManagementService.unregisterPlayerFromAllMatches(player, new UserContext());

        // Then
        verify(matchDAO).unregisterPlayerFromAllMatches(any(Player.class));
    }

    @Test
    void cancelValidMatch() throws Exception {
        // Given
        final Player player = new Player(1);
        player.setEmail("user@email.com");

        final Match match = new Match(1);
        match.setDate(LocalDateTime.now().plusDays(1));
        match.setStatus(CREATED);
        match.setCreator(player);

        final UserContext userContext = new UserContext("user@email.com");

        // When
        matchManagementService.cancelMatch(match, userContext);

        // Then
        assertEquals(CANCELLED, match.getStatus());
        verify(matchDAO).updateMatchStatus(any(Match.class));
    }

    @Test
    void cancelPastMatch() throws Exception {
        // Given
        final Player player = new Player(1);
        player.setEmail("user@email.com");

        final Match match = new Match(1);
        match.setDate(LocalDateTime.now().minusDays(1));
        match.setStatus(PLAYED);
        match.setCreator(player);

        final UserContext userContext = new UserContext("user@email.com");

        // When
        assertThrows(PastMatchException.class, () ->
                matchManagementService.cancelMatch(match, userContext));

        // Then
        verify(matchDAO, never()).updateMatchStatus(any(Match.class));
    }

    @Test
    void cancelMatchInvalidPlayer() throws Exception {
        // Given
        final Player player = new Player(1);
        player.setEmail("user@email.com");

        final Match match = new Match(1);
        match.setDate(LocalDateTime.now().plusDays(1));
        match.setStatus(CREATED);
        match.setCreator(player);

        UserContext userContext = new UserContext("another.user@email.com");

        // When
        assertThrows(UnauthorisedUserException.class, () ->
                matchManagementService.cancelMatch(match, userContext));

        // Then
        verify(matchDAO, never()).updateMatchStatus(any(Match.class));
    }
}
