package net.andresbustamante.yafoot.model;

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

    public User() {}

    public User(String email) {
        this.email = email;
    }

    public User(String email, String password, String surname, String firstName) {
        this.email = email;
        this.password = password;
        this.surname = surname;
        this.firstName = firstName;
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

    public void setSurname(String jouNom) {
        this.surname = jouNom;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String jouPrenom) {
        this.firstName = jouPrenom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(firstName, user.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, surname, firstName);
    }
}
