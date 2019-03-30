package net.andresbustamante.yafoot.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author andresbustamante
 */
public class AbstractController {

    private final Logger log = LoggerFactory.getLogger(AbstractController.class);

    protected URI getLocationURI(String location) {
        try {
            return new URI(location);
        } catch (URISyntaxException e) {
            log.error("Erreur lors de la construction de la localisation d'une ressource", e);
            return null;
        }
    }
}