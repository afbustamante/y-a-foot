package net.andresbustamante.yafoot.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.andresbustamante.yafoot.commons.model.Auditable;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Registration from a player to a match
 *
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
public class Registration implements Serializable, Auditable {

    private static final long serialVersionUID = 1L;

    /**
     * Registration ID between the match and the player
     */
    private RegistrationId id;

    /**
     * The car chosen for the registration
     */
    private Car car;

    /**
     * The player registered to the match
     */
    private Player player;

    /**
     * Indicates if the car chosen for the match has been confirmed by its owner
     */
    private boolean carConfirmed;

    /**
     * First date and time of the registration
     */
    private OffsetDateTime creationDate;

    /**
     * Date and time of the last update for the registration
     */
    private OffsetDateTime lastUpdateDate;

    public Registration(RegistrationId id) {
        this.id = id;
    }

    public Registration(int matchId, int playerId) {
        this.id = new RegistrationId(matchId, playerId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registration that = (Registration) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Registration [ id=" + id + " ]";
    }

}
