package net.andresbustamante.yafoot.core.model.enums;

import lombok.Getter;

/**
 * Enum of sports supported by the application
 */
@Getter
public enum SportEnum {

    FOOTBALL(1),
    RUGBY(2),
    BASKETBALL(3),
    VOLLEYBALL(4),
    HANDBALL(5),
    TENNIS(6),
    HOCKEY(7),
    BASEBALL(8),
    CRICKET(9),
    BADMINTON(10),
    FUTSAL(11),
    ICE_HOCKEY(12),
    SWIMMING(13),
    TABLE_TENNIS(14),
    OTHER(99);

    private final Integer id;

    SportEnum(Integer id) {
        this.id = id;
    }
}
