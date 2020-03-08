package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.PlayerDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.*;
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
    private PlayerDAO playerDAO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void saveMatchUsingExistingSite() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        Site site = new Site(1);
        Match match = new Match();
        match.setDateMatch(ZonedDateTime.now().plusDays(1));
        match.setSite(site);
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(matchDAO.isCodeAlreadyRegistered(anyString())).thenReturn(false);
        //when(playerDAO.findPlayerById(anyInt())).thenReturn(joueur);
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(joueur);
        when(siteDAO.chercherSiteParId(anyInt())).thenReturn(site);
        boolean succes = matchManagementService.saveMatch(match, ctx);

        // Then
        verify(matchDAO, times(1)).isCodeAlreadyRegistered(anyString());
        verify(siteDAO, times(1)).chercherSiteParId(any());
        verify(siteDAO, times(0)).creerSite(any());

        assertNotNull(match.getCode());
        assertNotNull(match.getCreateur());
        assertEquals(joueur, match.getCreateur());
        assertTrue(succes);
    }

    @Test
    void saveMatchUsingNewSite() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        Site site = new Site();
        Match match = new Match();
        match.setDateMatch(ZonedDateTime.now().plusDays(1));
        match.setSite(site);
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(matchDAO.isCodeAlreadyRegistered(anyString())).thenReturn(false);
        //when(playerDAO.findPlayerById(anyInt())).thenReturn(joueur);
        when(playerDAO.findPlayerByEmail(anyString())).thenReturn(joueur);
        boolean succes = matchManagementService.saveMatch(match, ctx);

        // Then
        verify(matchDAO, times(1)).isCodeAlreadyRegistered(anyString());
        verify(siteDAO, times(0)).chercherSiteParId(any());

        assertNotNull(match.getCode());
        assertNotNull(match.getCreateur());
        assertEquals(joueur, match.getCreateur());
        assertTrue(succes);
    }

    @Test
    void registerPlayerWithNoCar() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        Match match = new Match(1);
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(playerDAO.findPlayerById(anyInt())).thenReturn(joueur);
        //when(playerDAO.findPlayerByEmail(anyString())).thenReturn(joueur);
        when(matchDAO.findMatchById(anyInt())).thenReturn(match);
        when(matchDAO.isPlayerRegistered(any(), any())).thenReturn(false);
        boolean succes = matchManagementService.joinMatch(joueur, match, null, ctx);

        // Then
        assertTrue(succes);
        verify(carDAO, times(0)).findCarById(anyInt());
        verify(carDAO, times(0)).saveCar(any(Voiture.class), any(Joueur.class));
        verify(matchDAO, times(1)).registerPlayer(joueur, match, null);
    }

    @Test
    void registerPlayerWithExistingCar() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        Match match = new Match(1);
        Voiture voiture = new Voiture(1);
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(voiture);
        when(playerDAO.findPlayerById(anyInt())).thenReturn(joueur);
        //when(playerDAO.findPlayerByEmail(anyString())).thenReturn(joueur);
        when(matchDAO.findMatchById(anyInt())).thenReturn(match);
        when(matchDAO.isPlayerRegistered(any(), any())).thenReturn(false);
        boolean succes = matchManagementService.joinMatch(joueur, match, voiture, ctx);

        // Then
        assertTrue(succes);
        verify(carDAO, times(1)).findCarById(anyInt());
        verify(carDAO, times(0)).saveCar(any(Voiture.class), any(Joueur.class));
        verify(matchDAO, times(1)).registerPlayer(joueur, match, voiture);
    }

    @Test
    void registerPlayerWithNewCar() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        Match match = new Match(1);
        Voiture voiture = new Voiture();
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // When
        when(playerDAO.findPlayerById(anyInt())).thenReturn(joueur);
        //when(playerDAO.findPlayerByEmail(anyString())).thenReturn(joueur);
        when(matchDAO.findMatchById(anyInt())).thenReturn(match);
        when(matchDAO.isPlayerRegistered(any(), any())).thenReturn(false);
        boolean succes = matchManagementService.joinMatch(joueur, match, voiture, ctx);

        // Then
        assertTrue(succes);
        verify(carDAO, times(0)).findCarById(anyInt());
        verify(carDAO, times(1)).saveCar(any(Voiture.class), any(Joueur.class));
        verify(matchDAO, times(1)).registerPlayer(joueur, match, voiture);
    }

    @Test
    void unregisterPlayer() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        Match match = new Match(1);
        match.setCode("code");
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // Then
        when(playerDAO.findPlayerById(anyInt())).thenReturn(joueur);
        //when(playerDAO.findPlayerByEmail(anyString())).thenReturn(joueur);
        when(matchDAO.findMatchByCode(anyString())).thenReturn(match);
        when(matchDAO.isPlayerRegistered(joueur, match)).thenReturn(true);
        boolean succes = matchManagementService.quitMatch(joueur, match, ctx);

        // When
        assertTrue(succes);
        verify(matchDAO, times(1)).findMatchByCode(anyString());
        verify(matchDAO, times(1)).isPlayerRegistered(any(), any());
        verify(matchDAO, times(1)).unregisterPlayer(any(), any());
    }

    @Test
    void unregisterPlayerFromAllMatchesWithNoRegistry() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        Match match = new Match(1);
        match.setCode("code");
        UserContext ctx = new UserContext();
        ctx.setUsername("test@email.com");

        // Then
        when(playerDAO.findPlayerById(anyInt())).thenReturn(joueur);
        //when(playerDAO.findPlayerByEmail(anyString())).thenReturn(joueur);
        when(matchDAO.findMatchByCode(anyString())).thenReturn(match);
        when(matchDAO.isPlayerRegistered(joueur, match)).thenReturn(false);

        try {
            matchManagementService.quitMatch(joueur, match, ctx);
            fail();
        } catch (DatabaseException e) {
            assertEquals("Impossible d'inscrire le joueur : objet inexistant", e.getMessage());
        }
    }
}