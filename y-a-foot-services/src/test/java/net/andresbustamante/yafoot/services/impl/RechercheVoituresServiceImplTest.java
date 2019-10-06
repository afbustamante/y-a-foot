package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.VoitureDAO;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Voiture;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

class RechercheVoituresServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private RechercheVoituresServiceImpl rechercheVoituresService;

    @Mock
    private VoitureDAO voitureDAO;

    @Test
    void chargerVoituresJoueur() throws Exception {
        // Given
        Joueur joueur = new Joueur(1);
        List<Voiture> voitures = Arrays.asList(new Voiture(1), new Voiture(2));
        when(voitureDAO.chargerVoituresJoueur(any(Joueur.class))).thenReturn(voitures);

        // When
        List<Voiture> voituresCharges = rechercheVoituresService.chargerVoituresJoueur(joueur, new Contexte());

        // Then
        assertNotNull(voituresCharges);
        assertEquals(voitures, voituresCharges);

        verify(voitureDAO).chargerVoituresJoueur(any(Joueur.class));
    }
}