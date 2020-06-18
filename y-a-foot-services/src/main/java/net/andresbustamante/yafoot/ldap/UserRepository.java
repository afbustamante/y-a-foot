package net.andresbustamante.yafoot.ldap;

import net.andresbustamante.yafoot.model.User;
import net.andresbustamante.yafoot.model.enums.RolesEnum;

/**
 * Interface de gestion et récupération des informations des utilisateurs de l'annuaire LDAP
 */
public interface UserRepository {

    /**
     * Ajouter une nouvelle entrée dans l'annuaire avec un nouvel utilisateur
     *
     * @param usr User à créer
     * @param role Rôle de l'utilisateur à déclarer dans l'annuaire
     */
    void saveUser(User usr, RolesEnum role);

    /**
     * Mettre à jour les informations basiques d'un utilisateur passé en paramètre dans l'annuaire LDAP
     *
     * @param usr
     */
    void updateUser(User usr);

    /**
     * Update a user's password
     *
     * @param usr User to update
     */
    void updatePassword(User usr);

    /**
     * Supprimer un utilisateur de l'annuaire LDAP
     *
     * @param usr User à supprimer
     */
    void deleteUser(User usr);

    /**
     * Find a user by using his email address
     *
     * @param email Email address to search
     * @return
     */
    User findUserByEmail(String email);

    /**
     * Authenticate user by using his email address and his password
     *
     * @param uid User's ID (email address)
     * @param password User's password
     * @return Returns user's details only if the authentication information is valid
     */
    User authenticateUser(String uid, String password);

    /**
     * Find a user by the token generated to reset his password
     *
     * @param token Token to search
     * @return The user found for this token or null if nobody is found
     */
    User findUserByToken(String token);

    /**
     * Sets a token for password change to the user
     *
     * @param token Token to set
     * @param user User to modify
     */
    void saveTokenForUser(String token, User user);

    /**
     * Unsets a token for password change to the user
     *
     * @param user User to update
     */
    void removeTokenForUser(User user);
}