package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.PastMatchException;
import net.andresbustamante.yafoot.exceptions.UserNotAuthorisedException;
import net.andresbustamante.yafoot.model.*;
import net.andresbustamante.yafoot.services.CarManagementService;
import net.andresbustamante.yafoot.services.CarpoolingService;
import net.andresbustamante.yafoot.services.MessagingService;
import net.andresbustamante.yafoot.services.SiteManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static net.andresbustamante.yafoot.model.enums.MatchStatusEnum.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchManagementServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private MatchManagementServiceImpl matchManagementService;

    @Mock
    private MatchDAO matchDAO;

    @Mock
    private SiteDAO siteDAO;

    @Mock
    private CarDAO carDAO;

    @Mock
    private SiteManagementService siteManagementService;

    @Mock
    private CarManagementService carManagementService;

    @Mock
    private PlayerDAO playerDAO;

    @Mock
    private CarpoolingService carpoolingService;

    @Mock
    private MessagingService messagingService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void saveMatchUsingExistingSite() throws Exception {
        // Given
        Player player = new Player(1);
        Site site = new Site(1);
        Match match = new Match(1);
        match.setDate(OffsetDateTime.now().plusDays(1L));
        match.setSite(site);
        UserContext ctx = new UserContext();
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
        Player player = new Player(1);
        Site site = new Site();
        Match match = new Match(1);
        match.setDate(OffsetDateTime.now().plusDays(1));
        match.setSite(site);
        UserContext ctx = new UserContext();
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
        Player player = new Player(1);
        Site site = new Site(100);
        Match match = new Match(1);
        match.setDate(OffsetDateTime.now().plusDays(1));
        match.setSite(site);
        UserContext ctx = new UserContext();
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
        Player player = new Player(1);
        Match match = new Match(1);
        match.setDate(OffsetDateTime.now().plusDays(1L));
        match.setRegistrations(Collections.emptyList());
        UserContext ctx = new UserContext();
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
        Player player = new Player(1);
        Match match = new Match(1);
        match.setDate(OffsetDateTime.now().plusDays(1L));
        match.setRegistrations(Collections.emptyList());
        Car car = new Car(1);
        UserContext ctx = new UserContext();
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
        Player player = new Player(1);
        Match match = new Match(1);
        match.setDate(OffsetDateTime.now().plusDays(1L));
        match.setRegistrations(Collections.emptyList());
        Car car = new Car(100);
        UserContext ctx = new UserContext();
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
        Player player1 = new Player(1);
        player1.setEmail("test@email.com");
        Player player2 = new Player(2);
        player2.setEmail("another@email.com");
        Match match = new Match(1);
        match.setDate(OffsetDateTime.now().plusDays(1L));
        match.setCarpoolingEnabled(true);
        match.setRegistrations(Collections.emptyList());
        Car car = new Car(100);
        car.setDriver(player2);
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(car);
        matchManagementService.registerPlayer(player1, match, car, ctx);

        // Then
        verify(carDAO).findCarById(anyInt());
        verify(carManagementService, never()).saveCar(any(Car.class), any(UserContext.class));
        verify(matchDAO).registerPlayer(any(Player.class), any(Match.class), any(Car.class), eq(false));
        verify(carpoolingService).processCarSeatRequest(any(Match.class), any(Player.class), any(Car.class), any(UserContext.class));
    }

    @Test
    void registerPlayerWithNewCar() throws Exception {
        // Given
        Player player = new Player(1);
        Match match = new Match(1);
        match.setDate(OffsetDateTime.now().plusDays(1L));
        match.setRegistrations(Collections.emptyList());
        Car car = new Car();
        UserContext ctx = new UserContext();
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
        Player player1 = new Player(1);
        player1.setEmail("player@email.com");
        Match match = new Match(1);
        match.setDate(OffsetDateTime.now().plusDays(1L));
        Registration registration1 = new Registration(new RegistrationId(match.getId(), player1.getId()));
        registration1.setPlayer(player1);
        match.setRegistrations(Collections.singletonList(registration1));
        UserContext ctx = new UserContext();
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
        Player player1 = new Player(1);
        player1.setEmail("player@email.com");
        Player player2 = new Player(2);
        player2.setEmail("player2@email.com");
        Car car1 = new Car(1);
        car1.setDriver(player1);
        Match match = new Match(1);
        match.setCarpoolingEnabled(true);
        match.setDate(OffsetDateTime.now().plusDays(1L));
        Registration registration1 = new Registration(new RegistrationId(match.getId(), player1.getId()));
        registration1.setPlayer(player1);
        registration1.setCar(car1);
        registration1.setCarConfirmed(true);
        Registration registration2 = new Registration(new RegistrationId(match.getId(), player2.getId()));
        registration2.setPlayer(player2);
        registration2.setCar(car1);
        registration2.setCarConfirmed(true);
        match.setRegistrations(List.of(registration1, registration2));
        UserContext ctx = new UserContext();
        ctx.setUsername("player@email.com");

        // When
        when(matchDAO.loadRegistration(any(Match.class), any(Player.class))).thenReturn(registration1);
        when(carDAO.findCarById(anyInt())).thenReturn(car1);
        assertDoesNotThrow(() -> matchManagementService.registerPlayer(player1, match, car1, ctx));

        // Then
        verify(carpoolingService).processTransportationChange(any(Match.class), any(Car.class), any(Car.class), any(UserContext.class));
        verify(matchDAO).unregisterPlayer(any(Player.class), any(Match.class));
        verify(matchDAO).registerPlayer(any(Player.class), any(Match.class), any(Car.class), anyBoolean());
    }

    @Test
    void registerPlayerWhenFullMatch() {
        // Given
        Player player = new Player(1);
        Match match = new Match(1);
        match.setDate(OffsetDateTime.now().plusDays(1L));
        match.setNumPlayersMax(2);
        Registration registration1 = new Registration();
        registration1.setPlayer(new Player(2, "Two", "Player", "player.two@email.com", ""));
        Registration registration2 = new Registration();
        registration2.setPlayer(new Player(3, "Three", "Player", "player.three@email.com", ""));
        match.setRegistrations(Arrays.asList(registration1, registration2));
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        assertThrows(ApplicationException.class, () -> matchManagementService.registerPlayer(player, match, null, ctx));

        // Then
        verify(matchDAO, never()).registerPlayer(any(Player.class), any(Match.class), any(Car.class), anyBoolean());
    }

    @Test
    void unregisterPlayer() throws Exception {
        // Given
        Player player = new Player(1);
        player.setEmail("test@email.com");
        Match match = new Match(1);
        match.setCreator(player);
        Registration registration = new Registration(new RegistrationId(1, 1));
        registration.setPlayer(player);
        match.setRegistrations(Collections.singletonList(registration));
        match.setCode("code");
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // Then
        matchManagementService.unregisterPlayer(player, match, ctx);

        // When
        verify(matchDAO).unregisterPlayer(any(), any());
    }

    @Test
    void unregisterPlayerWhenNotRegistered() {
        // Given
        Player player = new Player(1);
        Match match = new Match(1);
        match.setCreator(new Player(2));
        match.setRegistrations(Collections.emptyList());
        match.setCode("code");
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        assertThrows(ApplicationException.class, () -> matchManagementService.unregisterPlayer(player, match, ctx));

        // Then
        verify(matchDAO, never()).unregisterPlayer(any(), any());
    }

    @Test
    void unregisterPlayerWithAlert() throws Exception {
        // Given
        Player player = new Player(1);
        player.setEmail("test@email.com");
        player.setPreferredLanguage(Locale.CHINESE.getLanguage());

        Match match = new Match(1);
        match.setCreator(player);
        match.setNumPlayersMin(1);
        match.setDate(OffsetDateTime.now().plusDays(7));

        Registration registration = new Registration(new RegistrationId(1, 1));
        registration.setPlayer(player);
        match.setRegistrations(Collections.singletonList(registration));
        match.setCode("code");
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // Then
        matchManagementService.unregisterPlayer(player, match, ctx);

        // When
        verify(matchDAO).unregisterPlayer(any(), any());
        verify(messagingService).sendEmail(anyString(), anyString(), any(String[].class), anyString(), any(MatchAlert.class), any(Locale.class));
    }

    @Test
    void unregisterPlayerFromAllMatches() throws Exception {
        // Given
        Player player = new Player(1);

        // When
        matchManagementService.unregisterPlayerFromAllMatches(player, new UserContext());

        // Then
        verify(matchDAO).unregisterPlayerFromAllMatches(any(Player.class));
    }

    @Test
    void cancelValidMatch() throws Exception {
        // Given
        Player player = new Player(1);
        player.setEmail("user@email.com");

        Match match = new Match(1);
        match.setDate(OffsetDateTime.now().plusDays(1));
        match.setStatus(CREATED);
        match.setCreator(player);

        UserContext userContext = new UserContext("user@email.com");

        // When
        matchManagementService.cancelMatch(match, userContext);

        // Then
        assertEquals(CANCELLED, match.getStatus());
        verify(matchDAO).updateMatchStatus(any(Match.class));
    }

    @Test
    void cancelPastMatch() throws Exception {
        // Given
        Player player = new Player(1);
        player.setEmail("user@email.com");

        Match match = new Match(1);
        match.setDate(OffsetDateTime.now().minusDays(1));
        match.setStatus(PLAYED);
        match.setCreator(player);

        UserContext userContext = new UserContext("user@email.com");

        // When
        assertThrows(PastMatchException.class, () ->
                matchManagementService.cancelMatch(match, userContext));

        // Then
        verify(matchDAO, never()).updateMatchStatus(any(Match.class));
    }

    @Test
    void cancelMatchInvalidPlayer() throws Exception {
        // Given
        Player player = new Player(1);
        player.setEmail("user@email.com");

        Match match = new Match(1);
        match.setDate(OffsetDateTime.now().plusDays(1));
        match.setStatus(CREATED);
        match.setCreator(player);

        UserContext userContext = new UserContext("another.user@email.com");

        // When
        assertThrows(UserNotAuthorisedException.class, () ->
                matchManagementService.cancelMatch(match, userContext));

        // Then
        verify(matchDAO, never()).updateMatchStatus(any(Match.class));
    }
}