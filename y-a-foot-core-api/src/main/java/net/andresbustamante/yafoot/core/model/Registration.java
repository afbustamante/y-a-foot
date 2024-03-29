package net.andresbustamante.yafoot.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.andresbustamante.yafoot.commons.model.Auditable;
import net.andresbustamante.yafoot.commons.model.Identifiable;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * Registration from a player to a match.
 *
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(of = {"id"}) @ToString(of = {"id"})
public final class Registration implements Identifiable<RegistrationId>, Serializable, Auditable {

    @Serial
    private static final long serialVersionUID = 2403452369516241329L;

    /**
     * Registration ID between the match and the player.
     */
    private RegistrationId id;

    /**
     * The car chosen for the registration.
     */
    private Car car;

    /**
     * The player registered to the match.
     */
    private Player player;

    /**
     * Indicates if the car chosen for the match has been confirmed by its owner.
     */
    private boolean carConfirmed;

    /**
     * First date and time of the registration.
     */
    private Instant creationDate;

    /**
     * Date and time of the last update for the registration.
     */
    private Instant modificationDate;

    /**
     * Constructor for testing purposes only.
     *
     * @param id Registration ID
     */
    public Registration(final RegistrationId id) {
        this.id = id;
    }

    /**
     * Constructor for testing purposes only.
     *
     * @param matchId Match ID
     * @param playerId Player ID
     */
    public Registration(final int matchId, final int playerId) {
        this.id = new RegistrationId(matchId, playerId);
    }
}
