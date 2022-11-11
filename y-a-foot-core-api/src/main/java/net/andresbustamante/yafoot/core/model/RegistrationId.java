package net.andresbustamante.yafoot.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Registration ID.
 *
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
public final class RegistrationId implements Serializable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationId that = (RegistrationId) o;
        return Objects.equals(matchId, that.matchId)
                && Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId, playerId);
    }

    @Override
    public String toString() {
        return "RegistrationId [ matchId=" + matchId + ", playerId=" + playerId + " ]";
    }

}
