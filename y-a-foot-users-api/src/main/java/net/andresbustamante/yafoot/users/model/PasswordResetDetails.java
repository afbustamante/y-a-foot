package net.andresbustamante.yafoot.users.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class PasswordResetDetails implements Serializable {

    /**
     * User's first name.
     */
    private String firstName;

    /**
     * Password reset link to send by email.
     */
    private String link;

    /**
     * Default constructor.
     *
     * @param firstName First name to use
     * @param link Link to use
     */
    public PasswordResetDetails(String firstName, String link) {
        this.firstName = firstName;
        this.link = link;
    }
}
