package net.andresbustamante.yafoot.web.vo;

import net.andresbustamante.yafoot.model.xs.Match;

/**
 * @author andresbustamante
 */
public class MatchVO extends Match {

    private String texteDate;
    private String texteJoueursAttendus;
    private String texteJoueursActuels;

    public String getTexteDate() {
        return texteDate;
    }

    public void setTexteDate(String texteDate) {
        this.texteDate = texteDate;
    }

    public String getTexteJoueursAttendus() {
        return texteJoueursAttendus;
    }

    public void setTexteJoueursAttendus(String texteJoueursAttendus) {
        this.texteJoueursAttendus = texteJoueursAttendus;
    }

    public String getTexteJoueursActuels() {
        return texteJoueursActuels;
    }

    public void setTexteJoueursActuels(String texteJoueursActuels) {
        this.texteJoueursActuels = texteJoueursActuels;
    }

    public Integer getNumJoueursInscrits() {
        return (this.inscriptions != null) ? this.inscriptions.getInscription().size() : 0;
    }
}
