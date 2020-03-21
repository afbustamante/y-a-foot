package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.Site;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static net.andresbustamante.yafoot.util.DaoConstants.*;

/**
 * @author andresbustamante
 */
public interface SiteDAO {

    /**
     * Chercher des sites d'intérêt pour un joueur.
     *
     * @param player Player to use for the research
     * @return Liste de sites ayant un lien avec le joueur passé en paramètre
     */
    List<Site> findSitesByPlayer(@Param(PLAYER) Player player);

    /**
     * Chercher un site par ID
     *
     * @param id Identifiant du site
     * @return Site associé à l'identifiant passé en paramètre
     */
    Site chercherSiteParId(@Param(ID) Integer id);

    /**
     * Créer le site passé en paramètre
     *
     * @param site Site à créer
     */
    void creerSite(@Param(SITE) Site site);
}
