package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.model.xs.Site;
import net.andresbustamante.yafoot.model.xs.Sites;
import net.andresbustamante.yafoot.util.ConfigProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

/**
 * @author andresbustamante
 */
public class OrganisationMatchsUIService extends AbstractUIService {

    private final Log log = LogFactory.getLog(OrganisationMatchsUIService.class);

    public OrganisationMatchsUIService() {
    }

    /**
     * Charger la liste de sites disponibles pour l'utilisateur connecté
     *
     * @return Liste de sites trouvés dans l'historique de l'utilisateur connecté
     * @throws ApplicationException
     */
    public List<Site> chercherSites() throws ApplicationException {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget webTarget = client.target(BASE_URI).path(ConfigProperties.getValue("recherche.sites.service.path"));
            Sites sites = webTarget.queryParam("idJoueur", getContexte().getUtilisateur().getId()).request(
                    MediaType.APPLICATION_XML).get(Sites.class);

            return (sites != null) ? sites.getSite() : Collections.emptyList();
        } catch (ResponseProcessingException ex) {
            log.error("Erreur lors de la recherche des sites pour un utilisateur", ex);
            throw new ApplicationException("Erreur lors de la recherche des sites : " + ex.getMessage());
        }
    }

    /**
     * Crée un nouveau match dans le système
     *
     * @param match Le match à créer
     * @return Le code du nouveau match
     * @throws ApplicationException
     */
    public String creerMatch(Match match) throws ApplicationException {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget webTarget = client.target(BASE_URI).path(ConfigProperties.getValue("gestion.matchs.service.path"));
            String location = webTarget.request(MediaType.APPLICATION_JSON).header(Contexte.UTILISATEUR,
                    getContexte().getUtilisateur().getId()).post(Entity.json(match)).getLocation().getPath();
            return location.substring(location.lastIndexOf("/"));
        } catch (ResponseProcessingException ex) {
            log.error("Erreur lors de la création du match", ex);
            throw new ApplicationException("Erreur lors de la recherche des sites : " + ex.getMessage());
        }
    }
}
