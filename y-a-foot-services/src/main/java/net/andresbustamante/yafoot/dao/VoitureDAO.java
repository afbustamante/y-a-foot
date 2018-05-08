package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Voiture;
import net.andresbustamante.yafoot.util.ConstantesDaoUtils;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;

/**
 * @author andresbustamante
 */
public interface VoitureDAO {

    Voiture chercherVoitureParId(@Param(ConstantesDaoUtils.ID) Integer id) throws SQLException;
}
