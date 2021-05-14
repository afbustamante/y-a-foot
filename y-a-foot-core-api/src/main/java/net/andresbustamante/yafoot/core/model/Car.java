package net.andresbustamante.yafoot.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.andresbustamante.yafoot.commons.model.Identifiable;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
public class Car implements Serializable, Identifiable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private Integer numSeats;
    private Integer numPassengers;
    private Player driver;

    public Car(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car that = (Car) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Car[ id=" + id + " ]";
    }

}
