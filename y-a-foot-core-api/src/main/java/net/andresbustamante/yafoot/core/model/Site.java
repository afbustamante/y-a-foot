package net.andresbustamante.yafoot.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.andresbustamante.yafoot.commons.model.Auditable;
import net.andresbustamante.yafoot.commons.model.GpsCoordinates;
import net.andresbustamante.yafoot.commons.model.Identifiable;
import net.andresbustamante.yafoot.commons.model.Locatable;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A site where a match can be played.
 *
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(of = {"id"}) @ToString(of = "id")
public final class Site implements Locatable, Identifiable<Integer>, Auditable, Serializable {

    @Serial
    private static final long serialVersionUID = 6333496998921053606L;

    private Integer id;
    private String name;
    private String address;
    private String postCode;
    private String city;
    private String country;
    private String phoneNumber;
    private GpsCoordinates location;
    private Instant creationDate;
    private Instant modificationDate;

    /**
     * Constructor for testing purposes only.
     *
     * @param id Site ID
     */
    public Site(Integer id) {
        this.id = id;
    }
}
