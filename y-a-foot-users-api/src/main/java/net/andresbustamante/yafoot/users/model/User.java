package net.andresbustamante.yafoot.users.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.andresbustamante.yafoot.commons.util.LocaleUtils;

import java.io.Serializable;

/**
 * Application user having an email as unique identifier.
 */
@Getter @Setter
@EqualsAndHashCode(of = {"email"}) @ToString(of = {"email"})
public class User implements Serializable {

    /**
     * Authentication ID.
     */
    private String email;
    /**
     * @deprecated No password needed since an external OAuth 2.0 resource server handles authentication
     */
    @Deprecated(since = "v2.0")
    private String password;
    private String surname;
    private String firstName;
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
}
