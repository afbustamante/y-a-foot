package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.JoueurDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.dao.SiteDAO;
import net.andresbustamante.yafoot.dao.VoitureDAO;
import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GestionMatchsServiceImplTest {

    @InjectMocks
    private GestionMatchsServiceImpl gestionMatchsService;

    @Mock
    private MatchDAO matchDAO;

    @Mock
    private SiteDAO siteDAO;

    @Mock
    private VoitureDAO voitureDAO;

    @Mock
    private JoueurDAO joueurDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void creerMatchSiteExistant() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        Site site = new Site(1);
        Match match = new Match();
        match.setDateMatch(ZonedDateTime.now().plusDays(1));
        match.setSite(site);
        Contexte ctx = new Contexte();
        ctx.setIdUtilisateur(1);

        // When
        when(matchDAO.isCodeExistant(anyString())).thenReturn(false);
        when(joueurDAO.chercherJoueurParId(anyInt())).thenReturn(joueur);
        when(siteDAO.chercherSiteParId(anyInt())).thenReturn(site);
        boolean succes = gestionMatchsService.creerMatch(match, ctx);

        // Then
        verify(matchDAO, times(1)).isCodeExistant(anyString());
        verify(joueurDAO, times(1)).chercherJoueurParId(anyInt());
        verify(siteDAO, times(1)).chercherSiteParId(any());
        verify(siteDAO, times(0)).creerSite(any());

        assertNotNull(match.getCode());
        assertNotNull(match.getCreateur());
        assertEquals(joueur, match.getCreateur());
        assertTrue(succes);
    }

    @Test
    public void creerMatchSiteInexistant() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        Site site = new Site();
        Match match = new Match();
        match.setDateMatch(ZonedDateTime.now().plusDays(1));
        match.setSite(site);
        Contexte ctx = new Contexte();
        ctx.setIdUtilisateur(1);

        // When
        when(matchDAO.isCodeExistant(anyString())).thenReturn(false);
        when(joueurDAO.chercherJoueurParId(anyInt())).thenReturn(joueur);
        when(siteDAO.chercherSiteParId(anyInt())).thenReturn(null);
        boolean succes = gestionMatchsService.creerMatch(match, ctx);

        // Then
        verify(matchDAO, times(1)).isCodeExistant(anyString());
        verify(joueurDAO, times(1)).chercherJoueurParId(anyInt());
        verify(siteDAO, times(0)).chercherSiteParId(any());

        assertNotNull(match.getCode());
        assertNotNull(match.getCreateur());
        assertEquals(joueur, match.getCreateur());
        assertTrue(succes);
    }

    @Test
    public void inscrireJoueurMatchSansVoiture() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        Match match = new Match(1);
        Contexte ctx = new Contexte();
        ctx.setIdUtilisateur(1);

        // When
        when(joueurDAO.chercherJoueurParId(anyInt())).thenReturn(joueur);
        when(matchDAO.chercherMatchParId(anyInt())).thenReturn(match);
        when(matchDAO.isJoueurInscritMatch(any(), any())).thenReturn(false);
        boolean succes = gestionMatchsService.inscrireJoueurMatch(joueur, match, null, ctx);

        // Then
        assertTrue(succes);
        verify(voitureDAO, times(0)).chercherVoitureParId(anyInt());
        verify(voitureDAO, times(0)).enregistrerVoiture(any());
        verify(matchDAO, times(1)).inscrireJoueurMatch(joueur, match, null);
    }

    @Test
    public void inscrireJoueurMatchAvecVoitureExistante() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        Match match = new Match(1);
        Voiture voiture = new Voiture(1);
        Contexte ctx = new Contexte();
        ctx.setIdUtilisateur(1);

        // When
        when(voitureDAO.chercherVoitureParId(anyInt())).thenReturn(voiture);
        when(joueurDAO.chercherJoueurParId(anyInt())).thenReturn(joueur);
        when(matchDAO.chercherMatchParId(anyInt())).thenReturn(match);
        when(matchDAO.isJoueurInscritMatch(any(), any())).thenReturn(false);
        boolean succes = gestionMatchsService.inscrireJoueurMatch(joueur, match, voiture, ctx);

        // Then
        assertTrue(succes);
        verify(voitureDAO, times(1)).chercherVoitureParId(anyInt());
        verify(voitureDAO, times(0)).enregistrerVoiture(any());
        verify(matchDAO, times(1)).inscrireJoueurMatch(joueur, match, voiture);
    }

    @Test
    public void inscrireJoueurMatchAvecNouvelleVoiture() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        Match match = new Match(1);
        Voiture voiture = new Voiture();
        Contexte ctx = new Contexte();
        ctx.setIdUtilisateur(1);

        // When
        when(joueurDAO.chercherJoueurParId(anyInt())).thenReturn(joueur);
        when(matchDAO.chercherMatchParId(anyInt())).thenReturn(match);
        when(matchDAO.isJoueurInscritMatch(any(), any())).thenReturn(false);
        boolean succes = gestionMatchsService.inscrireJoueurMatch(joueur, match, voiture, ctx);

        // Then
        assertTrue(succes);
        verify(voitureDAO, times(0)).chercherVoitureParId(anyInt());
        verify(voitureDAO, times(1)).enregistrerVoiture(any());
        verify(matchDAO, times(1)).inscrireJoueurMatch(joueur, match, voiture);
    }

    @Test
    public void desinscrireJoueurMatch() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        Match match = new Match(1);
        match.setCode("code");
        Contexte ctx = new Contexte();

        // Then
        when(joueurDAO.chercherJoueurParId(anyInt())).thenReturn(joueur);
        when(matchDAO.chercherMatchParCode(anyString())).thenReturn(match);
        when(matchDAO.isJoueurInscritMatch(joueur, match)).thenReturn(true);
        boolean succes = gestionMatchsService.desinscrireJoueurMatch(joueur, match, ctx);

        // When
        assertTrue(succes);
        verify(joueurDAO, times(1)).chercherJoueurParId(anyInt());
        verify(matchDAO, times(1)).chercherMatchParCode(anyString());
        verify(matchDAO, times(1)).isJoueurInscritMatch(any(), any());
        verify(matchDAO, times(1)).desinscrireJoueurMatch(any(), any());
    }

    @Test
    public void desinscrireJoueurNonInscritMatch() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        Match match = new Match(1);
        match.setCode("code");
        Contexte ctx = new Contexte();

        // Then
        when(joueurDAO.chercherJoueurParId(anyInt())).thenReturn(joueur);
        when(matchDAO.chercherMatchParCode(anyString())).thenReturn(match);
        when(matchDAO.isJoueurInscritMatch(joueur, match)).thenReturn(false);

        try {
            gestionMatchsService.desinscrireJoueurMatch(joueur, match, ctx);
            fail();
        } catch (BDDException e) {
            assertEquals("Impossible d'inscrire le joueur : objet inexistant", e.getMessage());
        }
    }
}