package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.model.Voiture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static net.andresbustamante.yafoot.util.DaoConstants.*;

/**
 * Interface de gestion et récupération des informations des voitures en base des données
 *
 * @author andresbustamante
 */
public interface CarDAO {

    /**
     * Récupérer les informations d'une voiture à partir de son identifiant
     *
     * @param id Identifiant de la voiture à chercher
     * @return
     */
    Voiture findCarById(@Param(ID) Integer id);

    /**
     * Créer une nouvelle voiture en base de données
     *
     * @param voiture Voiture à enregistrer
     * @param player Player registering the car
     */
    int saveCar(@Param(CAR) Voiture voiture, @Param(PLAYER) Player player);

    /**
     * Deletes all the cars registered by a player
     *
     * @param player
     */
    int deleteCarsByPlayer(@Param(PLAYER) Player player);

    /**
     * Loads the list of cars that a player has registered in database
     *
     * @param player The player to use for the research
     * @return
     */
    List<Voiture> findCarsByPlayer(@Param(PLAYER) Player player);
}
