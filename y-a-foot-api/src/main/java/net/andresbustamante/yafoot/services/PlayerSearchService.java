package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.UserContext;
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
     * @param userContext
     * @return
     * @throws DatabaseException
     */
    Joueur findPlayerByEmail(String email, UserContext userContext) throws DatabaseException;
}
