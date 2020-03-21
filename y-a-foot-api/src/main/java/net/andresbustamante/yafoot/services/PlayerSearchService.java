package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Player;

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
     * @return
     * @throws DatabaseException
     */
    Player findPlayerByEmail(String email) throws DatabaseException;
}
