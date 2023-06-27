package net.andresbustamante.yafoot.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.andresbustamante.yafoot.commons.model.Auditable;
import net.andresbustamante.yafoot.commons.model.Identifiable;
import net.andresbustamante.yafoot.users.model.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A match player.
 *
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {"id"}) @ToString(of = {"id"})
public final class Player extends User implements Serializable, Identifiable<Integer>, Auditable {

    @Serial
    private static final long serialVersionUID = -835073717353039623L;

    private Integer id;
    private String phoneNumber;
    private boolean active;
    private Instant creationDate;
    private Instant modificationDate;

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
        super(email, surname, firstName);
        this.id = id;
        this.phoneNumber = phoneNumber;
    }
}
