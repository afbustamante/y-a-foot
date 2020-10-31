package net.andresbustamante.yafoot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneId;

/**
 * @author andresbustamante
 */
@Getter @Setter @NoArgsConstructor
public class UserContext {

    public static final String TZ = "tz";

    private String username;
    private ZoneId timezone;

    public UserContext(String username) {
        this.username = username;
    }
}
