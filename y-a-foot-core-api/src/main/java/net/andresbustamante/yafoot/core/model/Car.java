package net.andresbustamante.yafoot.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.andresbustamante.yafoot.commons.model.Identifiable;

import java.io.Serializable;
import java.util.Objects;

/**
 * A car used to go to a match.
 *
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
public final class Car implements Serializable, Identifiable<Integer> {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    /**
     * Number of carpooling seats available on this car.
     */
    private Integer numSeats;
    /**
     * Number of passengers using this car to go to a given match.
     */
    private Integer numPassengers;
    private Player driver;

    /**
     * Constructor for testing purposes only.
     *
     * @param id Car ID
     */
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
