package net.andresbustamante.yafoot.core.dao;

import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Player;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static net.andresbustamante.yafoot.core.util.DaoConstants.*;

/**
 * Interface for loading and saving car data in database
 *
 * @author andresbustamante
 */
public interface CarDAO {

    /**
     * Loads a car details from database
     *
     * @param id Car's unique identifier
     * @return
     */
    Car findCarById(@Param(ID) Integer id);

    /**
     * Creates the car used in parameter into the database
     *
     * @param car Car to save
     */
    int saveCar(@Param(CAR) Car car);

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

    /**
     * Returs the list of cars registered by players for a given match
     *
     * @param match Match to search
     * @return List of cars found for the match
     */
    List<Car> findCarsByMatch(@Param(MATCH) Match match);
}
