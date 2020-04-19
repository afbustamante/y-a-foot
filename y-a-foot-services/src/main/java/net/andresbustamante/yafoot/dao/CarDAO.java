package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Car;
import net.andresbustamante.yafoot.model.Player;
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
    Car findCarById(@Param(ID) Integer id);

    /**
     * Créer une nouvelle voiture en base de données
     *
     * @param car Voiture à enregistrer
     * @param player Player registering the car
     */
    int saveCar(@Param(CAR) Car car, @Param(PLAYER) Player player);

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
    List<Car> findCarsByPlayer(@Param(PLAYER) Player player);
}
