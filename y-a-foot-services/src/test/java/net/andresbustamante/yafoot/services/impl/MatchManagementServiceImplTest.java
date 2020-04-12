package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.dao.SiteDAO;
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
        match.setDateMatch(ZonedDateTime.now().plusDays(1));
        match.setSite(site);
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(matchDAO.isCodeAlreadyRegistered(anyString())).thenReturn(false);
        when(matchDAO.findMatchById(anyInt())).thenReturn(match);
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);
        when(playerDAO.findPlayerById(anyInt())).thenReturn(player);
        when(siteDAO.findSiteById(anyInt())).thenReturn(site);
        matchManagementService.saveMatch(match, ctx);

        // Then
        verify(matchDAO, times(1)).isCodeAlreadyRegistered(anyString());
        verify(siteDAO, times(1)).findSiteById(any());
        verify(siteDAO, times(0)).saveSite(any());

        assertNotNull(match.getCode());
        assertNotNull(match.getCreateur());
        assertEquals(player, match.getCreateur());
    }

    @Test
    void saveMatchUsingNewSite() throws Exception {
        // Given
        Player player = new Player(1);
        Site site = new Site();
        Match match = new Match(1);
        match.setDateMatch(ZonedDateTime.now().plusDays(1));
        match.setSite(site);
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(matchDAO.isCodeAlreadyRegistered(anyString())).thenReturn(false);
        when(matchDAO.findMatchById(anyInt())).thenReturn(match);
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(player);
        when(playerDAO.findPlayerById(anyInt())).thenReturn(player);
        matchManagementService.saveMatch(match, ctx);

        // Then
        verify(matchDAO, times(1)).isCodeAlreadyRegistered(anyString());
        verify(siteDAO, times(0)).findSiteById(any());

        assertNotNull(match.getCode());
        assertNotNull(match.getCreateur());
        assertEquals(player, match.getCreateur());
    }

    @Test
    void registerPlayerWithNoCar() throws Exception {
        // Given
        Player player = new Player(1);
        Match match = new Match(1);
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(playerDAO.findPlayerById(anyInt())).thenReturn(player);
        when(matchDAO.findMatchById(anyInt())).thenReturn(match);
        when(matchDAO.isPlayerRegistered(any(), any())).thenReturn(false);
        matchManagementService.joinMatch(player, match, null, ctx);

        // Then
        verify(carDAO, times(0)).findCarById(anyInt());
        verify(carDAO, times(0)).saveCar(any(Voiture.class), any(Player.class));
        verify(matchDAO, times(1)).registerPlayer(player, match, null);
    }

    @Test
    void registerPlayerWithExistingCar() throws Exception {
        // Given
        Player player = new Player(1);
        Match match = new Match(1);
        Voiture voiture = new Voiture(1);
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(voiture);
        when(playerDAO.findPlayerById(anyInt())).thenReturn(player);
        when(matchDAO.findMatchById(anyInt())).thenReturn(match);
        when(matchDAO.isPlayerRegistered(any(), any())).thenReturn(false);
        matchManagementService.joinMatch(player, match, voiture, ctx);

        // Then
        verify(carDAO, times(1)).findCarById(anyInt());
        verify(carDAO, times(0)).saveCar(any(Voiture.class), any(Player.class));
        verify(matchDAO, times(1)).registerPlayer(player, match, voiture);
    }

    @Test
    void registerPlayerWithNewCar() throws Exception {
        // Given
        Player player = new Player(1);
        Match match = new Match(1);
        Voiture voiture = new Voiture();
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(playerDAO.findPlayerById(anyInt())).thenReturn(player);
        when(matchDAO.findMatchById(anyInt())).thenReturn(match);
        when(matchDAO.isPlayerRegistered(any(), any())).thenReturn(false);
        matchManagementService.joinMatch(player, match, voiture, ctx);

        // Then
        verify(carDAO, times(0)).findCarById(anyInt());
        verify(matchDAO, times(1)).registerPlayer(player, match, voiture);
    }

    @Test
    void unregisterPlayer() throws Exception {
        // Given
        Player player = new Player(1);
        Match match = new Match(1);
        match.setCode("code");
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // Then
        when(playerDAO.findPlayerById(anyInt())).thenReturn(player);
        when(matchDAO.findMatchByCode(anyString())).thenReturn(match);
        when(matchDAO.isPlayerRegistered(player, match)).thenReturn(true);
        matchManagementService.quitMatch(player, match, ctx);

        // When
        verify(matchDAO, times(1)).findMatchByCode(anyString());
        verify(matchDAO, times(1)).isPlayerRegistered(any(), any());
        verify(matchDAO, times(1)).unregisterPlayer(any(), any());
    }

    @Test
    void unregisterPlayerFromAllMatchesWithNoRegistry() throws Exception {
        // Given
        Player player = new Player(1);
        Match match = new Match(1);
        match.setCode("code");
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // Then
        when(playerDAO.findPlayerById(anyInt())).thenReturn(player);
        when(matchDAO.findMatchByCode(anyString())).thenReturn(match);
        when(matchDAO.isPlayerRegistered(player, match)).thenReturn(false);

        try {
            matchManagementService.quitMatch(player, match, ctx);
            fail();
        } catch (DatabaseException e) {
            assertEquals("Impossible d'inscrire le player : objet inexistant", e.getMessage());
        }
    }
}