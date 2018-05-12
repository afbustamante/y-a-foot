package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.util.ConstantesDaoUtils;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;

/**
 * @author andresbustamante
 */
public interface JoueurDAO {

    /**
     *
     * @param idJoueur
     * @return
     * @throws SQLException
     */
    Joueur chercherJoueurParId(@Param(ConstantesDaoUtils.ID) Integer idJoueur) throws SQLException;

    /**
     *
     * @param email
     * @return
     * @throws SQLException
     */
    boolean isJoueurInscrit(@Param(ConstantesDaoUtils.EMAIL) String email) throws SQLException;

    /**
     *
     * @param joueur
     * @throws SQLException
     */
    void creerJoueur(@Param(ConstantesDaoUtils.JOUEUR) Joueur joueur) throws SQLException;

    /**
     *
     * @param joueur
     * @throws SQLException
     */
    void actualiserJoueur(@Param(ConstantesDaoUtils.JOUEUR) Joueur joueur) throws SQLException;

    /**
     *
     * @param email
     * @return
     * @throws SQLException
     */
    Joueur chercherJoueurParEmail(@Param(ConstantesDaoUtils.EMAIL) String email) throws SQLException;
}
