package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Car;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.Match;
import org.apache.ibatis.annotations.Param;

import java.time.ZonedDateTime;
import java.util.List;

import static net.andresbustamante.yafoot.util.DaoConstants.*;

/**
 * Interface de récupération et modification des informations des matchs en base des données
 *
 * @author andresbustamante
 */
public interface MatchDAO {

    /**
     * Vérifier si le code passé en paramètre a déjà été utilisé pour identifier un match
     *
     * @param codeMatch Code à vérifier
     * @return
     */
    boolean isCodeAlreadyRegistered(@Param(CODE) String codeMatch);

    /**
     * Récupérer les informations d'un match à partir de son code unique
     *
     * @param codeMatch Code du match à récupérer
     * @return
     */
    Match findMatchByCode(@Param(CODE) String codeMatch);

    /**
     * Find the list of matches associated to a player by optionally using a date interval
     *
     * @param player Player to use for the research
     * @param startDate Start date for the research
     * @param endDate Limit date for the research
     * @return List of matches returned by the research
     */
    List<Match> findMatchesByPlayer(@Param(PLAYER) Player player, @Param(START_DATE) ZonedDateTime startDate,
                                    @Param(END_DATE) ZonedDateTime endDate);

    /**
     * Récupérer les informations d'un match à partir de son identifiant unique
     *
     * @param id Identifiant du match
     * @return
     */
    Match findMatchById(@Param(ID) Integer id);

    /**
     * Créer un match en base des données
     *
     * @param match Match à créer
     */
    void saveMatch(@Param(MATCH) Match match);

    /**
     * Inscrire un joueur à un match existant
     *
     * @param player Player to register
     * @param match Match auxquel le player doit s'inscrire
     * @param car Voiture du player pour assister au match (optionnelle)
     */
    void registerPlayer(@Param(PLAYER) Player player,
                             @Param(MATCH) Match match,
                             @Param(CAR) Car car);

    /**
     * Vérifier si un joueur est déjà inscrit à un match passé en paramètre
     *
     * @param player Player to search
     * @param match Match à chercher
     * @return
     */
    boolean isPlayerRegistered(@Param(PLAYER) Player player,
                                 @Param(MATCH) Match match);

    /**
     * Désinscrire un player d'un match
     *
     * @param player Player to unregister
     * @param match Match concerné
     */
    void unregisterPlayer(@Param(PLAYER) Player player,
                                @Param(MATCH) Match match);

    /**
     * Désinscrire un joueur de tous les matchs auxquels il était inscrit même dans le passé
     *
     * @param player Player to unregister
     */
    int unregisterPlayerFromAllMatches(@Param(PLAYER) Player player);
}
