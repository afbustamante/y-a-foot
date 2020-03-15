package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.Joueur;

/**
 * Service de gestion des joueurs
 *
 * @author andresbustamante
 */
public interface PlayerManagementService {

    /**
     * Inscrire un joueur dans le système
     *
     * @param joueur Joueur à créer
     * @param userContext Contexte de l'utilisateur réalisant l'action
     * @return New player's identifier
     * @throws DatabaseException
     * @throws LdapException
     */
    Integer savePlayer(Joueur joueur, UserContext userContext) throws LdapException, DatabaseException, ApplicationException;

    /**
     * Mettre à jour les informations de base d'un joeur
     *
     * @param joueur Joueur à mettre à jour
     * @param userContext Contexte de l'utilisateur réalisant l'action
     * @return Succès de l'opération
     * @throws DatabaseException
     * @throws LdapException
     */
    boolean updatePlayer(Joueur joueur, UserContext userContext) throws LdapException, DatabaseException;

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
