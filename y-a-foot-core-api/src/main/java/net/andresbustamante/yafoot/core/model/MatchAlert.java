package net.andresbustamante.yafoot.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor
public class MatchAlert implements Serializable {

    private String creatorFirstName;
    private String matchDate;
    private Integer numMinPlayers;
    private String link;
}
