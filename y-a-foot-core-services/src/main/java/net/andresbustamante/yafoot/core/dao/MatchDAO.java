package net.andresbustamante.yafoot.core.dao;

import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Registration;
import net.andresbustamante.yafoot.core.model.enums.MatchStatusEnum;
import net.andresbustamante.yafoot.core.model.enums.SportEnum;
import org.apache.ibatis.annotations.Param;

import java.time.OffsetDateTime;
import java.util.List;

import static net.andresbustamante.yafoot.core.util.DaoConstants.*;

/**
 * Interface describing the operations allowed with matches on database
 *
 * @author andresbustamante
 */
public interface MatchDAO {

    /**
     * Verifies if the code passed as parameter has already been used for another match
     *
     * @param code Code to check
     * @return True or false if the code is already in use or not
     */
    boolean isCodeAlreadyRegistered(@Param(CODE) String code);

    /**
     * Loads a match and its details by using its unique code
     *
     * @param code Code to look for
     * @return The match using the code and its details
     */
    Match findMatchByCode(@Param(CODE) String code);

    /**
     * Find the list of matches associated to a player by optionally using a date interval
     *
     * @param player Player to use for the research
     * @param sport Sport to search in the list of matches
     * @param status Match status to search
     * @param startDate Start date for the research
     * @param endDate Limit date for the research
     * @return List of matches returned by the research
     */
    List<Match> findMatchesByPlayer(@Param(PLAYER) Player player,
                                    @Param(SPORT) SportEnum sport,
                                    @Param(STATUS) MatchStatusEnum status,
                                    @Param(START_DATE) OffsetDateTime startDate,
                                    @Param(END_DATE) OffsetDateTime endDate);

    /**
     * Loads a match and its details by using its unique numeric identifier
     *
     * @param id Match technical ID
     * @return The match identified by this number and its details
     */
    Match findMatchById(@Param(ID) Integer id);

    /**
     * Creates a match on database
     *
     * @param match Match to create
     */
    void saveMatch(@Param(MATCH) Match match);

    /**
     * Registers a player to an existing match
     *
     * @param player Player to register
     * @param match Match to update
     * @param car Car used to go to the match (optional)
     * @param isCarConfirmed Indicates if there's a confirmation for assisting to a match with the selected car
     */
    int registerPlayer(@Param(PLAYER) Player player,
                        @Param(MATCH) Match match,
                        @Param(CAR) Car car, @Param("carConfirmation") Boolean isCarConfirmed);

    /**
     * Checks if a player is already registered to a match
     *
     * @param player Player to search
     * @param match Match to search
     * @return
     */
    boolean isPlayerRegistered(@Param(PLAYER) Player player,
                                 @Param(MATCH) Match match);

    /**
     * Unregisters a player from a match
     *
     * @param player Player to unregister
     * @param match Match to update
     */
    void unregisterPlayer(@Param(PLAYER) Player player,
                                @Param(MATCH) Match match);

    /**
     * Unregisters a player from all the matches where he/her was registered even past matches
     *
     * @param player Player to unregister
     */
    int unregisterPlayerFromAllMatches(@Param(PLAYER) Player player);

    /**
     * Updates and confirms a car for a player registered into a match
     *
     * @param match Match to search
     * @param player Player to search
     * @param car Car to set
     * @param isCarConfirmed Is the car confirmed or not
     */
    int updateCarForRegistration(@Param(MATCH) Match match, @Param(PLAYER) Player player, @Param(CAR) Car car,
                                  @Param("confirmed") boolean isCarConfirmed);

    /**
     * Loads registration details for a player in a specific match
     *
     * @param match Match to search
     * @param player Player to search
     * @return Registration details
     */
    Registration loadRegistration(@Param(MATCH) Match match, @Param(PLAYER) Player player);

    /**
     * Finds the list of passenger registrations for a given car
     *
     * @param match Match to search
     * @param car Car to search
     * @return List of registrations made with the given car for a match
     */
    List<Registration> findPassengerRegistrationsByCar(@Param(MATCH) Match match, @Param(CAR) Car car);

    /**
     * Resets car details for a player registration in a given match
     *
     * @param match Match to search
     * @param player Player to search
     * @return Number of lines touched by this operation
     */
    int resetCarDetails(@Param(MATCH) Match match, @Param(PLAYER) Player player);

    /**
     * Update a match with new status details
     *
     * @param match Match to update
     */
    void updateMatchStatus(@Param(MATCH) Match match);
}
