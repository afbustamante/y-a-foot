package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Site;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

import static net.andresbustamante.yafoot.util.DaoConstants.*;

/**
 * @author andresbustamante
 */
public interface SiteDAO {

    /**
     * Chercher des sites d'intérêt pour un joueur.
     *
     * @param idJoueur Identifiant du joueur
     * @return Liste de sites ayant un lien avec le joueur passé en paramètre
     */
    List<Site> chercherSitesPourJoueur(@Param(JOUEUR) Integer idJoueur) throws SQLException;

    /**
     * Chercher un site par ID
     *
     * @param id Identifiant du site
     * @return Site associé à l'identifiant passé en paramètre
     * @throws SQLException
     */
    Site chercherSiteParId(@Param(ID) Integer id) throws SQLException;

    /**
     * Créer le site passé en paramètre
     *
     * @param site Site à créer
     * @throws SQLException
     */
    void creerSite(@Param(SITE) Site site) throws SQLException;
}
