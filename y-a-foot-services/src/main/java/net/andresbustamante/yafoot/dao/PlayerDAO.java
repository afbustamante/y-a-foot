package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Player;
import org.apache.ibatis.annotations.Param;

import static net.andresbustamante.yafoot.util.DaoConstants.*;

/**
 * Interface de récupération et modification des informations des joueurs en base des données
 *
 * @author andresbustamante
 */
public interface PlayerDAO {

    /**
     * Chercher un joueur par son identifiant dans le système
     *
     * @param playerId Identifiant du joueur
     * @return Le joueur correspondant à l'identifiant passé en paramètre. Null si l'identifiant n'est pas utilisé
     */
    Player findPlayerById(@Param(ID) Integer playerId);

    /**
     * Vérifier si une address mail est déjà utilisée par un joueur inscrit
     *
     * @param email Adresse mail à chercher
     * @return True si un joueur existe avec l'address mail passée en paramètre
     */
    boolean isPlayerAlreadySignedUp(@Param(EMAIL) String email);

    /**
     * Créer un player en base des données
     *
     * @param player Player à créer
     */
    int savePlayer(@Param(PLAYER) Player player);

    /**
     * Mettre à jour les informations personnelles d'un player passé en paramètre
     *
     * @param player Player à mettre à jour
     */
    int updatePlayer(@Param(PLAYER) Player player);

    /**
     * Chercher un joueur en base des données à partir de son address mail
     *
     * @param email Adresse mail à chercher
     * @return Player associé à l'address mail passée en paramètre
     */
    Player findPlayerByEmail(@Param(EMAIL) String email);

    /**
     * Supprimer définitivement un player de la base des données
     *
     * @param player Player à supprimer
     */
    int deletePlayer(@Param(PLAYER) Player player);


    /**
     * Anonymiser les données d'un player et désactiver de mannière logique
     *
     * @param player Player à désactiver
     */
    int deactivatePlayer(@Param(PLAYER) Player player);
}
