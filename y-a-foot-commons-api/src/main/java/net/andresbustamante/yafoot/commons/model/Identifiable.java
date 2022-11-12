package net.andresbustamante.yafoot.commons.model;

/**
 * Interface to implement by any object stored in database. It must use a numeric technical identifier.
 * Excepted for entities representing table associations as they use a composite key
 *
 * @param <K> ID type (Short, Integer, Long...?)
 */
@FunctionalInterface
public interface Identifiable<K> {

    /**
     * Gets an object's unique identifier.
     *
     * @return Object identifier
     */
    K getId();
}
