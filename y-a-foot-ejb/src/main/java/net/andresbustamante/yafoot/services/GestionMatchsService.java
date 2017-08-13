package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Voiture;

import javax.ejb.Local;

/**
 * @author andresbustamante
 */
@Local
public interface GestionMatchsService {

    /**
     *
     * @param match
     * @param contexte
     * @throws BDDException
     */
    void creerMatch(Match match, Contexte contexte) throws BDDException;

    /**
     *
     * @param joueur
     * @param match
     * @param voiture
     * @param contexte
     * @throws BDDException
     */
    void inscrireJoueurMatch(Joueur joueur, Match match, Voiture voiture, Contexte contexte) throws BDDException;
}
