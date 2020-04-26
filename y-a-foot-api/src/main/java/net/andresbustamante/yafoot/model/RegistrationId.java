package net.andresbustamante.yafoot.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author andresbustamante
 */
public class RegistrationId implements Serializable {

    private Integer matchId;
    private Integer playerId;

    public RegistrationId() {}

    public RegistrationId(Integer matchId, Integer playerId) {
        this.matchId = matchId;
        this.playerId = playerId;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationId that = (RegistrationId) o;
        return Objects.equals(matchId, that.matchId) &&
                Objects.equals(playerId, that.playerId);
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
