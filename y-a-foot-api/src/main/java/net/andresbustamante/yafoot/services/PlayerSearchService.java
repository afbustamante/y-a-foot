package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;

/**
 * Service de recherche de joueurs
 *
 * @author andresbustamante
 */
public interface PlayerSearchService {

    /**
     * Chercher un joueur par son address mail unique
     *
     * @param email Adresse mail à chercher
     * @param contexte
     * @return
     * @throws DatabaseException
     */
    Joueur findPlayerByEmail(String email, Contexte contexte) throws DatabaseException;
}
