package net.andresbustamante.yafoot.model.enums;

import java.util.EnumSet;
import java.util.Set;

/**
 * Enumeration of matches' possible statuses
 */
public enum MatchStatusEnum {
    /** In-memory, not stored yet */
    DRAFT,
    /** Created in database */
    CREATED,
    /** Confirmed as played */
    PLAYED,
    /** Confirmed as cancelled */
    CANCELLED;

    /**
     * Set of statuses where no action is possible from users over a match
     */
    private static final Set<MatchStatusEnum> INACTIVE_STATUSES = EnumSet.of(PLAYED, CANCELLED);

    /**
     * @return True if this status is not in the list of inactive statuses for a match
     */
    public boolean isActiveStatus() {
        return !INACTIVE_STATUSES.contains(this);
    }
}
