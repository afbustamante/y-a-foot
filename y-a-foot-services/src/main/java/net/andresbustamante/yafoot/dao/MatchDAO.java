package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Voiture;
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
     * Trouver les matchs auxquels un joueur a été inscrit depuis la date passée en paramètre
     *
     * @param idJoueur Identifiant du joueur à chercher
     * @param dateInitiale Date initiale de la recherche
     * @return
     */
    List<Match> findMatchesByPlayer(@Param(ID) Integer idJoueur, @Param(DATE) ZonedDateTime dateInitiale);

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
     * @param joueur Joueur à inscrire
     * @param match Match auxquel le joueur doit s'inscrire
     * @param voiture Voiture du joueur pour assister au match (optionnelle)
     */
    void registerPlayer(@Param(PLAYER) Joueur joueur,
                             @Param(MATCH) Match match,
                             @Param(CAR) Voiture voiture);

    /**
     * Vérifier si un joueur est déjà inscrit à un match passé en paramètre
     *
     * @param joueur Joueur à vérifier
     * @param match Match à chercher
     * @return
     */
    boolean isPlayerRegistered(@Param(PLAYER) Joueur joueur,
                                 @Param(MATCH) Match match);

    /**
     * Désinscrire un joueur d'un match
     *
     * @param joueur Joueur à désinscrire
     * @param match Match concerné
     */
    void unregisterPlayer(@Param(PLAYER) Joueur joueur,
                                @Param(MATCH) Match match);

    /**
     * Désinscrire un joueur de tous les matchs auxquels il était inscrit même dans le passé
     *
     * @param joueur Joueur à désinscrire
     */
    int unregisterPlayerFromAllMatches(@Param(PLAYER) Joueur joueur);

    /**
     * Mettre à jour les informations du match passé en paramètre par rapport au nombre de joueurs
     * inscrits pour le match avec une nouvelle inscription
     *
     * @param match
     * @return
     */
    int notifyPlayerRegistry(@Param(MATCH) Match match);

    /**
     * Mettre à jour les informations du match passé en paramètre par rapport au nombre de joueurs
     * inscrits pour le match avec une inscription de moins
     *
     * @param match
     * @return
     */
    int notifyPlayerLeft(@Param(MATCH) Match match);
}
