package net.andresbustamante.yafoot.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.andresbustamante.yafoot.commons.model.Auditable;
import net.andresbustamante.yafoot.commons.model.Identifiable;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * A car used to go to a match.
 *
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(of = {"id"}) @ToString(of = {"id"})
public final class Car implements Serializable, Identifiable<Integer>, Auditable {

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
    private OffsetDateTime creationDate;
    private OffsetDateTime lastUpdateDate;

    /**
     * Constructor for testing purposes only.
     *
     * @param id Car ID
     */
    public Car(Integer id) {
        this.id = id;
    }
}
