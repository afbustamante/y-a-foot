package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.util.ConfigProperties;
import net.andresbustamante.yafoot.util.MessagesProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;

/**
 * Service du frontend pour l'inscription des nouveaux joueurs à l'application
 */
public class InscriptionJoueursUIService extends AbstractUIService {

    private final Logger log = LoggerFactory.getLogger(InscriptionJoueursUIService.class);

    /**
     * Créer un nouveau joueur dans l'application
     *
     * @param joueur Joueur à créer
     * @throws ApplicationException
     */
    public boolean creerNouveauCompteJoueur(Joueur joueur) throws ApplicationException {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget webTarget = client.target(BASE_URI).path(ConfigProperties.getValue("gestion.joueurs.service.path"));
            return webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(joueur), Boolean.class);
        } catch (ResponseProcessingException | BadRequestException ex) {
            log.error("Erreur lors de la création du joueur", ex);
            throw new ApplicationException(MessagesProperties.getValue("sign.in.error.existing.player", getLocaleUtilisateur()));
        }
    }
}
