package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.UserContext;

/**
 * Service de gestion des joueurs
 *
 * @author andresbustamante
 */
public interface PlayerManagementService {

    /**
     * Inscrire un player dans le système
     *
     * @param player Player à créer
     * @param userContext Contexte de l'utilisateur réalisant l'action
     * @return New player's identifier
     * @throws DatabaseException
     * @throws LdapException
     */
    Integer savePlayer(Player player, UserContext userContext) throws LdapException, DatabaseException, ApplicationException;

    /**
     * Mettre à jour les informations de base d'un joeur
     *
     * @param player Player à mettre à jour
     * @param userContext Contexte de l'utilisateur réalisant l'action
     * @return Succès de l'opération
     * @throws DatabaseException
     * @throws LdapException
     * @throws ApplicationException
     */
    boolean updatePlayer(Player player, UserContext userContext) throws LdapException, DatabaseException, ApplicationException;

    /**
     * Désactiver un joueur tout en supprimant son historique dans l'application et en supprimant
     * l'entrée dans l'annuaire LDAP
     *
     * @param playerId Player's identifier
     * @param userContext Contexte de l'utilisateur réalisant l'action
     * @throws DatabaseException
     * @throws LdapException
     */
    void deactivatePlayer(Integer playerId, UserContext userContext) throws LdapException, DatabaseException;
}
