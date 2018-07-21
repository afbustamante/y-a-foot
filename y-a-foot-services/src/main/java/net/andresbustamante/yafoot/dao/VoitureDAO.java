package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Voiture;
import net.andresbustamante.yafoot.util.ConstantesDaoUtils;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;

/**
 * Interface de gestion et récupération des informations des voitures en base des données
 *
 * @author andresbustamante
 */
public interface VoitureDAO {

    /**
     * Récupérer les informations d'une voiture à partir de son identifiant
     *
     * @param id Identifiant de la voiture à chercher
     * @return
     * @throws SQLException
     */
    Voiture chercherVoitureParId(@Param(ConstantesDaoUtils.ID) Integer id) throws SQLException;
}
