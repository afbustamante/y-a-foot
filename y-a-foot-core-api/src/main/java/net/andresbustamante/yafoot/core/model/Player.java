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
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
public class Player extends User implements Serializable, Identifiable, Auditable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String phoneNumber;
    private boolean active;
    private OffsetDateTime creationDate;
    private OffsetDateTime lastUpdateDate;
    private List<Car> cars;

    public Player(Integer id) {
        this.id = id;
    }

    public Player(Integer id, String surname, String firstName, String email, String phoneNumber) {
        this.id = id;
        this.surname = surname;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Player that = (Player) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, email);
    }

    @Override
    public String toString() {
        return "Player[ id=" + id + " ]";
    }

}
