package net.andresbustamante.yafoot.web.controllers;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author andresbustamante
 */
@Path("/utilisateurs")
public class UtilisateursController {

    @PUT
    @Path("/{email}/recuperationMotDePasse")
    public boolean recupererMdpOublie(@PathParam("email") String email) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
