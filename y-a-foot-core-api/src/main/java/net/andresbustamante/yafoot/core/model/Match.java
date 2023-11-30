package net.andresbustamante.yafoot.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.andresbustamante.yafoot.commons.model.Auditable;
import net.andresbustamante.yafoot.commons.model.Identifiable;
import net.andresbustamante.yafoot.core.model.enums.MatchStatusEnum;
import net.andresbustamante.yafoot.core.model.enums.SportEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static net.andresbustamante.yafoot.core.model.enums.MatchStatusEnum.CREATED;
import static net.andresbustamante.yafoot.core.model.enums.MatchStatusEnum.DRAFT;

/**
 * A match planned by a player.
 *
 * @author andresbustamante
 */
@Getter @Setter
@EqualsAndHashCode(of = {"id", "code"}) @ToString(of = {"id", "code"})
public final class Match implements Serializable, Identifiable<Integer>, Auditable {

    @Serial
    private static final long serialVersionUID = -483060416138264717L;

    private Integer id;
    private LocalDateTime date;
    private String code;
    private String description;
    private MatchStatusEnum status;
    private SportEnum sport;
    private Integer numPlayersMin;
    private Integer numPlayersMax;
    private List<Registration> registrations;
    private Site site;
    private Player creator;
    private boolean carpoolingEnabled;
    private boolean codeSharingEnabled;
    private Instant creationDate;
    private Instant modificationDate;

    /**
     * Main constructor.
     */
    public Match() {
        this.status = DRAFT;
    }

    /**
     * Constructor for testing purposes.
     *
     * @param id Identifier to give to the match
     */
    public Match(final Integer id) {
        this.id = id;
        this.status = CREATED;
    }

    /**
     * Indicates whether a player is registered on the current match.
     *
     * @param player Player to check
     * @return True if the player is already registered on this match
     */
    public boolean isPlayerRegistered(final Player player) {
        if (registrations != null) {
            return registrations.stream().anyMatch(registration -> registration.getPlayer().equals(player));
        }
        return false;
    }

    /**
     * A match is accepting registrations if the date of the match is in the future and the number of players expected
     * is still higher than the number of players registered for the match.
     *
     * @return True if the match is still accepting registrations
     */
    public boolean isAcceptingRegistrations() {
        if (status.isActiveStatus() && LocalDateTime.now().isBefore(date)) {
            return numPlayersMax == null || numPlayersMax > getNumRegisteredPlayers();
        } else {
            return false;
        }
    }

    /**
     * Counts the number of registered players on the current match.
     *
     * @return Number of registered players
     */
    public Integer getNumRegisteredPlayers() {
        if (registrations != null) {
            return registrations.size();
        }
        return 0;
    }
}
