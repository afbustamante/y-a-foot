package net.andresbustamante.yafoot.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.andresbustamante.yafoot.commons.model.Identifiable;

import java.io.Serial;
import java.io.Serializable;

/**
 * A sport to practice in a match.
 */
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(of = {"id"}) @ToString(of = "id")
public final class Sport implements Serializable, Identifiable<Short> {

    @Serial
    private static final long serialVersionUID = -1482022215011838737L;

    private Short id;
    private String code;
    private String name;

    /**
     * Constructor for testing purposes only.
     *
     * @param id Sport ID
     * @param code Sport code
     */
    public Sport(Short id, String code) {
        this.id = id;
        this.code = code;
    }
}
