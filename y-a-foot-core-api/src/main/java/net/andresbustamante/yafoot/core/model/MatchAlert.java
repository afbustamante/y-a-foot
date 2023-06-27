package net.andresbustamante.yafoot.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor
public final class MatchAlert implements Serializable {

    @Serial
    private static final long serialVersionUID = 4276382586749183411L;

    private String creatorFirstName;
    private String matchDate;
    private Integer numMinPlayers;
    private String link;
}
