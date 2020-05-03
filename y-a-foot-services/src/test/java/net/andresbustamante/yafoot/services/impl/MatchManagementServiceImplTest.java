package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.*;
import net.andresbustamante.yafoot.services.CarManagementService;
import net.andresbustamante.yafoot.services.SiteManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;

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
        match.setDate(ZonedDateTime.now().plusDays(1));
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
        match.setDate(ZonedDateTime.now().plusDays(1));
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
        match.setDate(ZonedDateTime.now().plusDays(1));
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
        match.setRegistrations(Collections.emptyList());
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        matchManagementService.registerPlayer(player, match, null, ctx);

        // Then
        verify(carDAO, never()).findCarById(anyInt());
        verify(carManagementService, never()).saveCar(any(Car.class), any(UserContext.class));
        verify(matchDAO).registerPlayer(player, match, null, false);
    }

    @Test
    void registerPlayerWithExistingCar() throws Exception {
        // Given
        Player player = new Player(1);
        Match match = new Match(1);
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
        verify(matchDAO).registerPlayer(any(Player.class), any(Match.class), any(Car.class), anyBoolean());
    }

    @Test
    void registerPlayerWithUnknownCar() throws Exception {
        // Given
        Player player = new Player(1);
        Match match = new Match(1);
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
    void registerPlayerWithNewCar() throws Exception {
        // Given
        Player player = new Player(1);
        Match match = new Match(1);
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
        Registration registration1 = new Registration(new RegistrationId(match.getId(), player1.getId()));
        registration1.setPlayer(player1);
        match.setRegistrations(Collections.singletonList(registration1));
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        assertThrows(ApplicationException.class, () -> matchManagementService.registerPlayer(player1, match, null, ctx));

        // Then
        verify(matchDAO, never()).registerPlayer(any(Player.class), any(Match.class), any(Car.class), anyBoolean());
    }

    @Test
    void registerPlayerWhenFullMatch() {
        // Given
        Player player = new Player(1);
        Match match = new Match(1);
        match.setNumPlayersMax(2);
        Registration registration1 = new Registration();
        Registration registration2 = new Registration();
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
    void unregisterPlayerFromAllMatches() throws Exception {
        // Given
        Player player = new Player(1);

        // When
        matchManagementService.unregisterPlayerFromAllMatches(player, new UserContext());

        // Then
        verify(matchDAO).unregisterPlayerFromAllMatches(any(Player.class));
    }
}