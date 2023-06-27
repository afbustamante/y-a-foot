package net.andresbustamante.yafoot.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor
public final class CarpoolingRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 4730524987993813064L;

    private String requesterFirstName;
    private String driverFirstName;
    private String link;
    private String matchDate;

}
