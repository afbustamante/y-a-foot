package net.andresbustamante.yafoot.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * Registration ID.
 *
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(of = {"matchId", "playerId"})
@ToString(of = {"matchId", "playerId"})
public final class RegistrationId implements Serializable {

    @Serial
    private static final long serialVersionUID = -8700388360563478154L;

    /**
     * Match ID for this registration.
     */
    private Integer matchId;

    /**
     * Player ID for this registration.
     */
    private Integer playerId;

    /**
     * Constructor for testing purposes only.
     *
     * @param matchId Match ID
     * @param playerId Player ID
     */
    public RegistrationId(Integer matchId, Integer playerId) {
        this.matchId = matchId;
        this.playerId = playerId;
    }
}
