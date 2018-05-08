package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Site;
import net.andresbustamante.yafoot.util.ConstantesDaoUtils;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

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
    List<Site> chercherSitesPourJoueur(@Param(ConstantesDaoUtils.JOUEUR) Integer idJoueur) throws SQLException;

    Site chercherSiteParId(@Param(ConstantesDaoUtils.ID) Integer id) throws SQLException;

    void creerSite(@Param(ConstantesDaoUtils.SITE) Site site) throws SQLException;
}
