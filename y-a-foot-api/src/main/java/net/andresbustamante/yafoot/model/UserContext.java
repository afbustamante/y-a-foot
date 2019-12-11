package net.andresbustamante.yafoot.model;

import java.time.ZoneId;

/**
 * @author andresbustamante
 */
public class UserContext {

    public static final String USER_CTX = "userCtx";
    public static final String TZ = "tz";

    private Integer userId;
    private String userEmail;
    private ZoneId timezone;

    public UserContext() {}

    public UserContext(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public ZoneId getTimezone() {
        return timezone;
    }

    public void setTimezone(ZoneId timezone) {
        this.timezone = timezone;
    }
}
