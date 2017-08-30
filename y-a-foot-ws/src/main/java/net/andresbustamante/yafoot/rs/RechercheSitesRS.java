package net.andresbustamante.yafoot.rs;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Site;
import net.andresbustamante.yafoot.services.RechercheSitesService;
import net.andresbustamante.yafoot.xs.Sites;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author andresbustamante
 */
@Path("/sites/recherche")
@RequestScoped
public class RechercheSitesRS {

    @EJB
    private RechercheSitesService rechercheSitesService;

    private final Log log = LogFactory.getLog(RechercheSitesRS.class);

    @GET
    @Path("/nom/{nom}")
    @Produces("application/json")
    public Response chercherSitesParNom(@PathParam("nom") String nom) {
        try {
            List<Site> sites = rechercheSitesService.chercherSitesParNom(nom, new Contexte());
            Sites response = new Sites();

            if (CollectionUtils.isNotEmpty(sites)) {
                for (Site site : sites) {
                    response.getSite().add(copierSite(site));
                }
            }

            return Response.ok(response).build();
        } catch (BDDException e) {
            log.error("Erreur lors de la recherche d'un site par nom", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private net.andresbustamante.yafoot.xs.Site copierSite(Site site) {
        net.andresbustamante.yafoot.xs.Site siteXml = new net.andresbustamante.yafoot.xs.Site();
        siteXml.setId(site.getId());
        siteXml.setNom(site.getNom());
        siteXml.setAdresse(site.getAdresse());
        siteXml.setNumeroTelephone(site.getTelephone());

        return siteXml;
    }
}
