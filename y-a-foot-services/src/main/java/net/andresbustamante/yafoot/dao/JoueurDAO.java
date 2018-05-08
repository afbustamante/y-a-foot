package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.util.ConstantesDaoUtils;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;

/**
 * @author andresbustamante
 */
public interface JoueurDAO {

    Joueur chercherJoueurParId(@Param(ConstantesDaoUtils.ID) Integer idJoueur) throws SQLException;

    boolean isJoueurInscrit(@Param(ConstantesDaoUtils.EMAIL) String email) throws SQLException;

    void creerJoueur(@Param(ConstantesDaoUtils.JOUEUR) Joueur joueur) throws SQLException;

    void actualiserJoueur(@Param(ConstantesDaoUtils.JOUEUR) Joueur joueur) throws SQLException;
}
