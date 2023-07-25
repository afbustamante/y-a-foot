package net.andresbustamante.yafoot.commons.events;

/**
 * Parent interface for all message listeners.
 *
 * @param <K> Type to convert from message
 */
public interface MessagingEventListener<K> {

    /**
     * Method to implement when an incoming message is received.
     *
     * @param message Message to receive
     */
    void onMessage(K message);
}
