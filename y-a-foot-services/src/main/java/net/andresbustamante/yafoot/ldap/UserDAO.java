package net.andresbustamante.yafoot.ldap;

import net.andresbustamante.yafoot.model.User;
import net.andresbustamante.yafoot.model.enums.RolesEnum;

/**
 * Interface de gestion et récupération des informations des utilisateurs de l'annuaire LDAP
 */
public interface UserDAO {

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
     * Supprimer un utilisateur de l'annuaire LDAP
     *  @param usr User à supprimer
     * @param roles Rôles de l'utilisateur à supprimer
     */
    void deleteUser(User usr, RolesEnum[] roles);

    /**
     * Chercher un utilisateur dans l'annuaire à partir de son identifiant
     *
     * @param uid Identifiant de l'utilisateur dans l'annuaire
     * @return
     */
    User findUserByUid(String uid);

    /**
     * Load user's details for authentication and authorisation
     *
     * @param uid User's ID
     * @return User's details including roles
     */
    User findUserAuthDetailsByUid(String uid);

    /**
     * Authenticate user by using his email address and his password
     *
     * @param uid User's ID (email address)
     * @param password User's password
     * @return Returns user's details only if the authentication information is valid
     */
    User authenticateUser(String uid, String password);
}
