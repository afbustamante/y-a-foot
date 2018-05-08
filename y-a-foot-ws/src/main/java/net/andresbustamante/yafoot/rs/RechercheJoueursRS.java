package net.andresbustamante.yafoot.rs;

import net.andresbustamante.yafoot.exceptions.BDDException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.services.GestionJoueursService;
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

/**
 * @author andresbustamante
 */
@Path("/joueurs/recherche")
@RequestScoped
public class RechercheJoueursRS {

    @Context
    private UriInfo context;

    @EJB
    private GestionJoueursService gestionJoueursService;

    private transient final Log log = LogFactory.getLog(RechercheJoueursRS.class);

    public RechercheJoueursRS() {
    }

    @GET
    @Path("/joueur/{email}")
    @Produces("application/xml")
    public Response getJoueurParEmail(@PathParam("email") String email) {
        Contexte contexte = new Contexte();

        try {
            Joueur joueur = gestionJoueursService.chercherJoueur(email, contexte);

            if (joueur != null) {
                return Response.ok(copierJoueur(joueur)).build();
            } else {
                return Response.ok().build();
            }
        } catch (BDDException e) {
            log.error("Erreur lors de la recherche d'un utilisateur", e);
            return Response.serverError().build();
        }
    }

    /**
     *
     * @param joueur
     * @return
     */
    private net.andresbustamante.yafoot.xs.Joueur copierJoueur(Joueur joueur) {
        net.andresbustamante.yafoot.xs.Joueur joueurXml = new net.andresbustamante.yafoot.xs.Joueur();
        joueurXml.setId(joueur.getId());
        joueurXml.setNom(joueur.getNom());
        joueurXml.setPrenom(joueur.getPrenom());
        joueurXml.setEmail(joueur.getEmail());
        return joueurXml;
    }
}
