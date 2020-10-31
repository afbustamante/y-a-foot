package net.andresbustamante.yafoot.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class PasswordResetDetails implements Serializable {

    private String firstName;
    private String link;

    public PasswordResetDetails(String firstName, String link) {
        this.firstName = firstName;
        this.link = link;
    }
}
