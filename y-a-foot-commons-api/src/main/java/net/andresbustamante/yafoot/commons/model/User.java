package net.andresbustamante.yafoot.commons.model;

import lombok.Getter;
import lombok.Setter;
import net.andresbustamante.yafoot.commons.util.LocaleUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Application user having an email as unique identifier
 */
@Getter @Setter
public class User implements Serializable {

    /**
     * Authentication ID
     */
    protected String email;
    protected String password;
    protected String surname;
    protected String firstName;
    /**
     * Authentication token
     */
    protected String token;
    protected String preferredLanguage;

    public User() {
        this.preferredLanguage = LocaleUtils.DEFAULT_LOCALE.getLanguage();
    }

    public User(String email) {
        this.preferredLanguage = LocaleUtils.DEFAULT_LOCALE.getLanguage();
        this.email = email;
    }

    public User(String email, String password, String surname, String firstName) {
        this.email = email;
        this.password = password;
        this.surname = surname;
        this.firstName = firstName;
        this.preferredLanguage = LocaleUtils.DEFAULT_LOCALE.getLanguage();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User [ email='" + email + " ]";
    }
}
