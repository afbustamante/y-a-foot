package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Voiture;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
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
    boolean isCodeExistant(@Param(CODE) String codeMatch) throws SQLException;

    /**
     * Récupérer les informations d'un match à partir de son code unique
     *
     * @param codeMatch Code du match à récupérer
     * @return
     */
    Match chercherMatchParCode(@Param(CODE) String codeMatch) throws SQLException;

    /**
     * Trouver les matchs auxquels un joueur a été inscrit
     *
     * @param idJoueur Identifiant du joueur à chercher
     * @return
     */
    List<Match> chercherMatchsParJoueur(@Param(ID) Integer idJoueur,
                                        @Param(DATE) ZonedDateTime dateInitiale)
            throws SQLException;

    /**
     * Récupérer les informations d'un match à partir de son identifiant unique
     *
     * @param id Identifiant du match
     * @return
     */
    Match chercherMatchParId(@Param(ID) Integer id) throws SQLException;

    /**
     * Créer un match en base des données
     *
     * @param match Match à créer
     */
    void creerMatch(@Param(MATCH) Match match) throws SQLException;

    /**
     * Inscrire un joueur à un match existant
     *
     * @param joueur Joueur à inscrire
     * @param match Match auxquel le joueur doit s'inscrire
     * @param voiture Voiture du joueur pour assister au match (optionnelle)
     */
    void inscrireJoueurMatch(@Param(JOUEUR) Joueur joueur,
                             @Param(MATCH) Match match,
                             @Param(VOITURE) Voiture voiture) throws SQLException;

    /**
     * Vérifier si un joueur est déjà inscrit à un match passé en paramètre
     *
     * @param joueur Joueur à vérifier
     * @param match Match à chercher
     * @return
     * @throws SQLException
     */
    boolean isJoueurInscritMatch(@Param(JOUEUR) Joueur joueur,
                                 @Param(MATCH) Match match) throws SQLException;

    /**
     * Désinscrire un joueur d'un match
     *
     * @param joueur Joueur à désinscrire
     * @param match Match concerné
     * @throws SQLException
     */
    void desinscrireJoueurMatch(@Param(JOUEUR) Joueur joueur,
                                @Param(MATCH) Match match) throws SQLException;
}
