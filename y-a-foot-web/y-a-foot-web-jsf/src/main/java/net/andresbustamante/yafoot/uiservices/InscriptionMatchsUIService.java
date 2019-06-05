package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.xs.Inscription;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Voiture;
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
     * Inscrire le joueur actif dans la session au match passé en paramètre
     *
     * @param match
     * @param voiture
     * @return
     * @throws ApplicationException
     */
    public boolean inscrireJoueurMatch(Match match, Voiture voiture) throws ApplicationException {
        Inscription inscription = new Inscription();
        inscription.setIdMatch(match.getId());
        inscription.setJoueur((Joueur) getContexte().getUtilisateur());

        if (voiture != null) {
            inscription.setVoiture(voiture);
        }

        try {
            Client client = ClientBuilder.newClient();
            WebTarget resource = client.target(BASE_URI).path(ConfigProperties.getValue("inscriptions.matchs.service.path"));

            return resource.request(MediaType.APPLICATION_JSON).header(Contexte.UTILISATEUR,
                    getContexte().getUtilisateur().getId()).post(Entity.json(inscription), Boolean.class);
        } catch (ResponseProcessingException ex) {
            log.error("Erreur lors de l'inscription d'un joueur à un match", ex);
            throw new ApplicationException("Erreur lors de l'inscription d'un joueur a un match : " + ex.getMessage());
        }
    }
}
