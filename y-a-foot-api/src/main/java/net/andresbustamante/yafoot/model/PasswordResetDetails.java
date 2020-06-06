package net.andresbustamante.yafoot.model;

public class PasswordResetDetails {

    private String firstName;
    private String link;

    public PasswordResetDetails(String firstName, String link) {
        this.firstName = firstName;
        this.link = link;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
