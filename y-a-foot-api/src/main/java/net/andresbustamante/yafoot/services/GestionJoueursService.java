package net.andresbustamante.yafoot.services;

import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;

/**
 * Service de gestion des joueurs
 *
 * @author andresbustamante
 */
public interface GestionJoueursService {

    /**
     * Inscrire un joueur dans le système
     *
     * @param joueur Joueur à créer
     * @param contexte Contexte de l'utilisateur réalisant l'action
     * @return Succès de l'opération
     * @throws DatabaseException
     * @throws LdapException
     */
    boolean inscrireJoueur(Joueur joueur, Contexte contexte) throws LdapException, DatabaseException;

    /**
     * Mettre à jour les informations de base d'un joeur
     *
     * @param joueur Joueur à mettre à jour
     * @param contexte Contexte de l'utilisateur réalisant l'action
     * @return Succès de l'opération
     * @throws DatabaseException
     * @throws LdapException
     */
    boolean actualiserJoueur(Joueur joueur, Contexte contexte) throws LdapException, DatabaseException;

    /**
     * Désactiver un joueur tout en supprimant son historique dans l'application et en supprimant
     * l'entrée dans l'annuaire LDAP
     *
     * @param emailJoueur Email du joueur à supprimer
     * @param contexte Contexte de l'utilisateur réalisant l'action
     * @return Succès de l'opération
     * @throws DatabaseException
     * @throws LdapException
     */
    boolean desactiverJoueur(String emailJoueur, Contexte contexte) throws LdapException, DatabaseException;
}
