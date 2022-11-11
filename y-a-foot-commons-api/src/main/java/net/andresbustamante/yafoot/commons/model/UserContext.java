package net.andresbustamante.yafoot.commons.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneId;

/**
 * Context information for the connected user.
 *
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
public class UserContext {

    /**
     * Timezone constant code.
     */
    public static final String TZ = "tz";

    /**
     * Username of the active user.
     */
    private String username;

    /**
     * Origin timezone of the active user.
     */
    private ZoneId timezone;

    /**
     * Constructor using a username.
     *
     * @param username Username for the new context object
     */
    public UserContext(String username) {
        this.username = username;
    }
}
