package net.andresbustamante.yafoot.model;

import java.io.Serializable;

/**
 * @author andresbustamante
 */
public class IdInscription implements Serializable {

    private Integer matchId;
    private Integer playerId;

    public IdInscription() {}

    public IdInscription(Integer matchId, Integer playerId) {
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
    public int hashCode() {
        int hash = 0;
        hash += matchId;
        hash += playerId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IdInscription)) {
            return false;
        }
        IdInscription other = (IdInscription) object;
        if (!this.matchId.equals(other.matchId)) {
            return false;
        }
        if (!this.playerId.equals(other.playerId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IdInscription [ matchId=" + matchId + ", playerId=" + playerId + " ]";
    }

}
