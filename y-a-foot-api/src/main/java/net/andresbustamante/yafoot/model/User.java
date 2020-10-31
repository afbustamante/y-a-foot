package net.andresbustamante.yafoot.model;

import net.andresbustamante.yafoot.util.LocaleUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Application user having an email as unique identifier
 */
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
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
