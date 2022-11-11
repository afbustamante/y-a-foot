package net.andresbustamante.yafoot.users.model;

import lombok.Getter;
import lombok.Setter;
import net.andresbustamante.yafoot.commons.util.LocaleUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Application user having an email as unique identifier.
 */
@Getter @Setter
public class User implements Serializable {

    /**
     * Authentication ID.
     */
    private String email;
    private String password;
    private String surname;
    private String firstName;
    /**
     * Authentication token.
     */
    private String token;
    /**
     * User's preferred language code. Example: "en" for English or "fr" for French.
     */
    private String preferredLanguage;

    /**
     * Default constructor.
     */
    public User() {
        this.preferredLanguage = LocaleUtils.DEFAULT_LOCALE.getLanguage();
    }

    /**
     * Constructor with an email address.
     *
     * @param email User's email
     */
    public User(String email) {
        this.preferredLanguage = LocaleUtils.DEFAULT_LOCALE.getLanguage();
        this.email = email;
    }

    /**
     * Constructor for testing purposes.
     *
     * @param email User's email
     * @param password User's password
     * @param surname User's surname/last name
     * @param firstName User's first name
     */
    public User(String email, String password, String surname, String firstName) {
        this.email = email;
        this.password = password;
        this.surname = surname;
        this.firstName = firstName;
        this.preferredLanguage = LocaleUtils.DEFAULT_LOCALE.getLanguage();
    }

    /**
     * Compares 2 users using their email address. Two users are the same when they have the same email address.
     *
     * @param o User to compare to
     * @return Whether the 2 users are the same or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    /**
     * Hash code using the user's email address.
     *
     * @return Hash code for this user
     */
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    /**
     * String using email address.
     *
     * @return User string with an email address.
     */
    @Override
    public String toString() {
        return "User [ email='" + email + " ]";
    }
}
