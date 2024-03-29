package net.andresbustamante.yafoot.core.dao;

import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Site;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static net.andresbustamante.yafoot.core.util.DaoConstants.*;

/**
 * Interface describing the operations allowed on sites in database.
 *
 * @author andresbustamante
 */
public interface SiteDao {

    /**
     * Looks for the sites related to a player. It will look in player's history to get the sites where the player has
     * played or is planning to go to.
     *
     * @param player Player to use for the research
     * @return List of sites found for the player
     */
    List<Site> findSitesByPlayer(@Param(PLAYER) Player player);

    /**
     * Loads a site by its technical ID.
     *
     * @param id Identifier to search
     * @return Site identified by the number used as parameter for this method
     */
    Site findSiteById(@Param(ID) Integer id);

    /**
     * Creates a site in database.
     *
     * @param site Site to create
     * @param author Player creating the site
     * @return Number of created sites. It should be 0 or 1
     */
    int saveSite(@Param(SITE) Site site, @Param(PLAYER) Player author);
}
