package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Voiture;
import net.andresbustamante.yafoot.util.ConstantesDaoUtils;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author andresbustamante
 */
public interface MatchDAO {

    /**
     *
     * @param codeMatch
     * @return
     */
    boolean isCodeExistant(@Param(ConstantesDaoUtils.CODE) String codeMatch) throws SQLException;

    /**
     *
     * @param codeMatch
     * @return
     */
    Match chercherMatchParCode(@Param(ConstantesDaoUtils.CODE) String codeMatch) throws SQLException;

    /**
     *
     * @param idJoueur
     * @return
     */
    List<Match> chercherMatchsParJoueur(@Param(ConstantesDaoUtils.ID) Integer idJoueur,
                                        @Param(ConstantesDaoUtils.DATE) Date dateInitiale)
            throws SQLException;

    /**
     *
     * @param id
     * @return
     */
    Match chercherMatchParId(@Param(ConstantesDaoUtils.ID) Integer id) throws SQLException;

    /**
     *
     * @param match
     */
    void creerMatch(@Param(ConstantesDaoUtils.MATCH) Match match) throws SQLException;

    /**
     *
     * @param joueur
     * @param match
     * @param voiture
     */
    void inscrireJoueurMatch(@Param(ConstantesDaoUtils.JOUEUR) Joueur joueur,
                             @Param(ConstantesDaoUtils.MATCH) Match match,
                             @Param(ConstantesDaoUtils.VOITURE) Voiture voiture) throws SQLException;

    /**
     *
     * @param joueur
     * @param match
     * @return
     * @throws SQLException
     */
    boolean isJoueurInscritMatch(@Param(ConstantesDaoUtils.JOUEUR) Joueur joueur,
                                 @Param(ConstantesDaoUtils.MATCH) Match match) throws SQLException;
}
