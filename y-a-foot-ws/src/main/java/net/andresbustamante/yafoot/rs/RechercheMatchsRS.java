package net.andresbustamante.yafoot.rs;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.*;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Site;
import net.andresbustamante.yafoot.services.RechercheMatchsService;
import net.andresbustamante.yafoot.util.DateUtils;
import net.andresbustamante.yafoot.xs.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Web Service REST pour la recherche et consultation des matches
 *
 * @author andresbustamante
 */
@Path("/matchs/recherche")
@RequestScoped
public class RechercheMatchsRS {

    @Context
    private UriInfo context;

    @EJB
    private RechercheMatchsService rechercheMatchsService;

    private final Log log = LogFactory.getLog(RechercheMatchsRS.class);

    public RechercheMatchsRS() {
    }

    @GET
    @Path("/code/{codeMatch}")
    @Produces("application/xml")
    public Response getMatchParCode(@PathParam("codeMatch") String codeMatch) {
        try {
            Match match = rechercheMatchsService.chercherMatchParCode(codeMatch, new Contexte());

            if (match != null) {
                net.andresbustamante.yafoot.xs.Match matchXml = copierMatch(match);
                return Response.ok(matchXml).build();
            } else {
                return Response.ok().build();
            }
        } catch (BDDException e) {
            log.error("Erreur de BD pour la recherche d'un match.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/joueur/{idJoueur}")
    @Produces("application/xml")
    public Response getMatchsJoueur(@PathParam("idJoueur") Integer idJoueur) {
        try {
            List<Match> matchs = rechercheMatchsService.chercherMatchsJoueur(idJoueur, new Contexte());

            if (matchs != null) {
                Matchs matchsXml = new Matchs();

                for (Match match : matchs) {
                    matchsXml.getMatch().add(copierMatch(match));
                }

                return Response.ok(matchsXml).build();
            } else {
                return Response.ok().build();
            }
        } catch (BDDException e) {
            log.error("Erreur de BD pour la recherche d'un match.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private net.andresbustamante.yafoot.xs.Match copierMatch(Match match) {
        net.andresbustamante.yafoot.xs.Match matchXml = new net.andresbustamante.yafoot.xs.Match();
        matchXml.setId(match.getId());
        matchXml.setCode(match.getCode());
        matchXml.setDate(DateUtils.transformer(match.getDateMatch()));
        matchXml.setDescription(match.getDescription());
        matchXml.setNumJoueursMin(match.getNumJoueursMin());
        matchXml.setNumJoueursMax(match.getNumJoueursMax());

        matchXml.setSite(copierSite(match.getSite()));

        if (match.getJoueursMatch() != null) {
            matchXml.setJoueurs(new Joueurs());

            for (JoueurMatch joueurMatch : match.getJoueursMatch()) {
                matchXml.getJoueurs().getJoueur().add(copierJoueurMatch(joueurMatch));
            }
        }

        return matchXml;
    }

    private net.andresbustamante.yafoot.xs.Joueur copierJoueurMatch(JoueurMatch joueurMatch) {
        net.andresbustamante.yafoot.xs.Joueur joueurXml = new net.andresbustamante.yafoot.xs.Joueur();
        joueurXml.setId(joueurMatch.getId().getIdJoueur());
        joueurXml.setPrenom(joueurMatch.getJoueur().getPrenom());

        return joueurXml;
    }

    private net.andresbustamante.yafoot.xs.Site copierSite(Site site) {
        net.andresbustamante.yafoot.xs.Site siteXml = new net.andresbustamante.yafoot.xs.Site();
        siteXml.setId(site.getId());
        siteXml.setAdresse(site.getAdresse());
        siteXml.setNom(site.getNom());

        return siteXml;
    }
}
