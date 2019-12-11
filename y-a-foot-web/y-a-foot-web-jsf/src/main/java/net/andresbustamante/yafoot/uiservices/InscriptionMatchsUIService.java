package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.model.xs.Car;
import net.andresbustamante.yafoot.model.xs.Player;
import net.andresbustamante.yafoot.model.xs.Registration;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.util.ConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;

/**
 * @author andresbustamante
 */
public class InscriptionMatchsUIService extends AbstractUIService {

    private final Logger log = LoggerFactory.getLogger(InscriptionMatchsUIService.class);

    /**
     * Inscrire le player actif dans la session au match passé en paramètre
     *
     * @param match
     * @param car
     * @return
     * @throws ApplicationException
     */
    public boolean inscrireJoueurMatch(Match match, Car car) throws ApplicationException {
        Registration registration = new Registration();
        registration.setMatchId(match.getId());
        registration.setPlayer((Player) getUserContext().getUser());

        if (car != null) {
            registration.setCar(car);
        }

        try {
            Client client = ClientBuilder.newClient();
            WebTarget resource = client.target(BASE_URI).path(ConfigProperties.getValue("registrations.api.service.path"));

            return resource.request(MediaType.APPLICATION_JSON).header(UserContext.USER_CTX,
                    getUserContext().getUser().getId()).post(Entity.json(registration), Boolean.class);
        } catch (ResponseProcessingException ex) {
            log.error("Erreur lors de l'inscription d'un player à un match", ex);
            throw new ApplicationException("Erreur lors de l'inscription d'un player a un match : " + ex.getMessage());
        }
    }
}
