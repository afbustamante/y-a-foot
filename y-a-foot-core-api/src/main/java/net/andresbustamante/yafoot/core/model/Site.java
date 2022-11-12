package net.andresbustamante.yafoot.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.andresbustamante.yafoot.commons.model.GpsCoordinates;
import net.andresbustamante.yafoot.commons.model.Identifiable;
import net.andresbustamante.yafoot.commons.model.Locatable;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author andresbustamante
 */
@Getter
@Setter
@NoArgsConstructor
public final class Site implements Locatable, Identifiable<Integer>, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String address;
    private String phoneNumber;
    private GpsCoordinates location;

    /**
     * Constructor for testing purposes only.
     *
     * @param id Site ID
     */
    public Site(Integer id) {
        this.id = id;
    }

    /**
     * Constructor for testing purposes only.
     *
     * @param name        Site name
     * @param address     Address
     * @param phoneNumber Phone number
     * @param location    Location
     */
    public Site(String name, String address, String phoneNumber, GpsCoordinates location) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site that = (Site) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Site[ id=" + id + " ]";
    }

}
