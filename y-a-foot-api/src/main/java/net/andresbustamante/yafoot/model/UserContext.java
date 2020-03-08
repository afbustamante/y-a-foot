package net.andresbustamante.yafoot.model;

import java.time.ZoneId;

/**
 * @author andresbustamante
 */
public class UserContext {

    public static final String TZ = "tz";

    private String username;
    private ZoneId timezone;

    public UserContext() {}

    public UserContext(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ZoneId getTimezone() {
        return timezone;
    }

    public void setTimezone(ZoneId timezone) {
        this.timezone = timezone;
    }
}
