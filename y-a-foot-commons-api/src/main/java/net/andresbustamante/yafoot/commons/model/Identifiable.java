package net.andresbustamante.yafoot.commons.model;

/**
 * Interface to implement by any object stored in database. It must use a numeric technical identifier.
 * Excepted for entities representing table associations as they use a composite key
 */
@FunctionalInterface
public interface Identifiable {
    
    Integer getId();
}
