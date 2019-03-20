package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.xs.Sites;
import net.andresbustamante.yafoot.services.RechercheSitesService;
import net.andresbustamante.yafoot.web.mappers.SiteMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author andresbustamante
 */
@Path("/sites")
public class SitesController {

    @Autowired
    private RechercheSitesService rechercheSitesService;

    private final Logger log = LoggerFactory.getLogger(SitesController.class);

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getSitesJoueur(@QueryParam("idJoueur") Integer idJoueur) {
        try {
            List<net.andresbustamante.yafoot.model.Site> sites = rechercheSitesService.chercherSitesParJoueur(idJoueur,
                    new Contexte());

            if (CollectionUtils.isNotEmpty(sites)) {
                Sites result = new Sites();

                for (net.andresbustamante.yafoot.model.Site site : sites) {
                    result.getSite().add(SiteMapper.INSTANCE.toSiteDTO(site));
                }
                return Response.ok(result).build();
            } else {
                return Response.ok(new Sites()).build();
            }
        } catch (BDDException e) {
            log.error("Erreur lors de la recherche de sites par joueur", e);
            return Response.serverError().build();
        }
    }
}
