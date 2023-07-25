package net.andresbustamante.yafoot.core.services;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;

/**
 * Service enabling alert controls on matches.
 */
public interface MatchAlertingService {

    /**
     * Checks whether a match has a new alert of cancelling risk after a player cancelled his registration to it. If an
     * alert is detected, it sends a notification to inform the author of the match of this alert.
     *
     * @param matchId Concerned match ID
     * @param playerId ID from the player that was unregistered from the match
     * @throws ApplicationException
     */
    void checkForAlertsAfterPlayerRemovedFromMatch(Integer matchId, Integer playerId) throws ApplicationException;
}
