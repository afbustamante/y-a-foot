package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.model.xs.Contexte;
import net.andresbustamante.yafoot.util.ConfigProperties;

/**
 * @author andresbustamante
 */
public abstract class AbstractUIService {

    protected static final String BASE_URI = ConfigProperties.getValue("rest.services.uri");

    private Contexte contexte;
}
