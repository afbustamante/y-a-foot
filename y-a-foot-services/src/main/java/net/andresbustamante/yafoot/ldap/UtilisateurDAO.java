package net.andresbustamante.yafoot.ldap;

import net.andresbustamante.yafoot.model.Utilisateur;
import net.andresbustamante.yafoot.model.enums.RolesEnum;

/**
 *
 */
public interface UtilisateurDAO {

    /**
     *
     * @param usr
     * @param role
     */
    void creerUtilisateur(Utilisateur usr, RolesEnum role);

    /**
     *
     * @param usr
     */
    void actualiserUtilisateur(Utilisateur usr);
}
