package net.andresbustamante.yafoot.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor
public class CarpoolingRequest implements Serializable {

    private String requesterFirstName;
    private String driverFirstName;
    private String link;
    private String matchDate;

}
