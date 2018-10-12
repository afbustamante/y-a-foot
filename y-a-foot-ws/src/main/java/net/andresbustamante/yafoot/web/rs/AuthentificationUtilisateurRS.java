package net.andresbustamante.yafoot.web.rs;

import net.andresbustamante.yafoot.model.xs.Utilisateur;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andresbustamante
 */
@RestController
public class AuthentificationUtilisateurRS {

    @PostMapping(path = "/utilisateurs/authentification")
    public void authentifierUtilisateur(@RequestParam("utilisateur") Utilisateur utilisateur) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @PostMapping(path = "/utilisateurs/recuperationMotDePasse")
    public boolean recupererMdpOublie(@RequestParam("email") String email) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
