package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Joueur;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Service du frontend pour l'inscription des nouveaux joueurs à l'application
 */
public class InscriptionJoueursUIService {

    private final Log log = LogFactory.getLog(InscriptionJoueursUIService.class);

    /**
     * Créer un nouveau joueur dans l'application
     *
     * @param joueur Joueur à créer
     * @throws ApplicationException
     */
    public void creerNouveauCompteJoueur(Joueur joueur) throws ApplicationException {
    }
}
