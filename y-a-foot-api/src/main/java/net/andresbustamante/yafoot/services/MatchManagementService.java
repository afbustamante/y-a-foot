package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Voiture;

/**
 * Service de gestion des matchs
 *
 * @author andresbustamante
 */
public interface MatchManagementService {

    /**
     * Créer un match dans le système
     *
     * @param match Match à créer
     * @param userContext
     * @throws DatabaseException
     */
    boolean saveMatch(Match match, UserContext userContext) throws DatabaseException;

    /**
     * Inscrire un joueur à un match existant
     *
     * @param joueur Joueur à inscrire dans le match
     * @param match Match à impacter avec cette inscription
     * @param voiture Voiture dans laquelle le joueur va se déplacer vers le site de jeu
     * @param userContext
     * @throws DatabaseException
     */
    boolean joinMatch(Joueur joueur, Match match, Voiture voiture, UserContext userContext) throws DatabaseException;

    /**
     * Désinscrire un joueur d'un match
     *
     * @param joueur Joueur à désinscrire
     * @param match Match concerné
     * @param userContext
     * @return
     * @throws DatabaseException
     */
    boolean quitMatch(Joueur joueur, Match match, UserContext userContext) throws DatabaseException;
}
