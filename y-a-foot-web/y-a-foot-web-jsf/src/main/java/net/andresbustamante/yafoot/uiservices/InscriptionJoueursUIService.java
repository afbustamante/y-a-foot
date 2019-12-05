package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Player;
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
     * Créer un nouveau player dans l'application
     *
     * @param player Joueur à créer
     * @throws ApplicationException
     */
    public boolean creerNouveauCompteJoueur(Player player) throws ApplicationException {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget webTarget = client.target(BASE_URI).path(ConfigProperties.getValue("players.api.service.path"));
            return webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(player), Boolean.class);
        } catch (ResponseProcessingException | BadRequestException ex) {
            log.error("Erreur lors de la création du player", ex);
            throw new ApplicationException(MessagesProperties.getValue("sign.in.error.existing.player", getLocaleUtilisateur()));
        }
    }
}
