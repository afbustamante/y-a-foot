package net.andresbustamante.yafoot.ldap;

import net.andresbustamante.yafoot.model.Utilisateur;
import net.andresbustamante.yafoot.model.enums.RolesEnum;

/**
 * Interface de gestion et récupération des informations des utilisateurs de l'annuaire LDAP
 */
public interface UserDAO {

    /**
     * Ajouter une nouvelle entrée dans l'annuaire avec un nouvel utilisateur
     *
     * @param usr Utilisateur à créer
     * @param role Rôle de l'utilisateur à déclarer dans l'annuaire
     */
    void saveUser(Utilisateur usr, RolesEnum role);

    /**
     * Mettre à jour les informations basiques d'un utilisateur passé en paramètre dans l'annuaire LDAP
     *
     * @param usr
     */
    void updateUser(Utilisateur usr);

    /**
     * Supprimer un utilisateur de l'annuaire LDAP
     *  @param usr Utilisateur à supprimer
     * @param roles Rôles de l'utilisateur à supprimer
     */
    void deleteUser(Utilisateur usr, RolesEnum[] roles);

    /**
     * Chercher un utilisateur dans l'annuaire à partir de son identifiant
     *
     * @param uid Identifiant de l'utilisateur dans l'annuaire
     * @return
     */
    Utilisateur findUserByUid(String uid);
}
