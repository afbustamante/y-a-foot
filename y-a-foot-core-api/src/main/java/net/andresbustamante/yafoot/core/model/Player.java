package net.andresbustamante.yafoot.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.andresbustamante.yafoot.commons.model.Auditable;
import net.andresbustamante.yafoot.commons.model.Identifiable;
import net.andresbustamante.yafoot.users.model.User;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

/**
 * A match player.
 *
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
public final class Player extends User implements Serializable, Identifiable, Auditable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String phoneNumber;
    private boolean active;
    private OffsetDateTime creationDate;
    private OffsetDateTime lastUpdateDate;

    /**
     * List of cars registered by this player.
     */
    private List<Car> cars;

    /**
     * Constructor for testing purposes only.
     *
     * @param id Player ID
     */
    public Player(Integer id) {
        this.id = id;
    }

    /**
     * Constructor for testing purposes only.
     *
     * @param id Player ID
     * @param surname Player's surname
     * @param firstName Player's first name
     * @param email Player's email
     * @param phoneNumber Player's phone number
     */
    public Player(Integer id, String surname, String firstName, String email, String phoneNumber) {
        super(email, null, surname, firstName);
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Player that = (Player) o;
        return Objects.equals(id, that.id) && Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, getEmail());
    }

    @Override
    public String toString() {
        return "Player[ id=" + id + " ]";
    }

}
