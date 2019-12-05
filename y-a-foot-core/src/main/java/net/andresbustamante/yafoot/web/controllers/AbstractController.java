package net.andresbustamante.yafoot.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author andresbustamante
 */
public abstract class AbstractController {

    private final Logger log = LoggerFactory.getLogger(AbstractController.class);

    protected URI getLocationURI(String location) {
        try {
            return new URI(location);
        } catch (URISyntaxException e) {
            log.error("Erreur lors de la construction de la location d'une ressource", e);
            return null;
        }
    }
}
