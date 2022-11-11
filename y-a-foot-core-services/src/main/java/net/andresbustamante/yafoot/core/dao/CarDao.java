package net.andresbustamante.yafoot.core.dao;

import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Player;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static net.andresbustamante.yafoot.core.util.DaoConstants.*;

/**
 * Interface for loading and saving car data in database.
 *
 * @author andresbustamante
 */
public interface CarDao {

    /**
     * Loads a car details from database.
     *
     * @param id Car's unique identifier
     * @return Car found for the given ID. Null if no car found
     */
    Car findCarById(@Param(ID) Integer id);

    /**
     * Creates the car used in parameter into the database.
     *
     * @param car Car to save
     * @return Number of created cars. It should be 0 or 1
     */
    int saveCar(@Param(CAR) Car car);

    /**
     * Deletes all the cars registered by a player.
     *
     * @param player
     * @return Number of deleted cars
     */
    int deleteCarsByPlayer(@Param(PLAYER) Player player);

    /**
     * Loads the list of cars that a player has registered in database.
     *
     * @param player The player to use for the research
     * @return List of cars found
     */
    List<Car> findCarsByPlayer(@Param(PLAYER) Player player);

    /**
     * Returs the list of cars registered by players for a given match.
     *
     * @param match Match to search
     * @return List of cars found for the match
     */
    List<Car> findCarsByMatch(@Param(MATCH) Match match);
}
