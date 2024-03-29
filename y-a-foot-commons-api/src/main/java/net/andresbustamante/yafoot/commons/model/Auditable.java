package net.andresbustamante.yafoot.commons.model;

import java.time.Instant;

/**
 * Interface to be implemented by any object needing to be audited.
 */
public interface Auditable {

    /**
     * Gets the date and time of the creation for this object.
     *
     * @return Creation date and time for this object
     */
    Instant getCreationDate();

    /**
     * Gets the date and time of the last update for this object.
     *
     * @return Date and time of the last update
     */
    Instant getModificationDate();
}
