package net.andresbustamante.yafoot.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.andresbustamante.yafoot.commons.model.Identifiable;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor
public final class Sport implements Serializable, Identifiable {

    private Integer id;
    private String code;
    private String name;

    /**
     * Constructor for testing purposes only.
     *
     * @param id Sport ID
     * @param code Sport code
     */
    public Sport(Integer id, String code) {
        this.id = id;
        this.code = code;
    }
}
