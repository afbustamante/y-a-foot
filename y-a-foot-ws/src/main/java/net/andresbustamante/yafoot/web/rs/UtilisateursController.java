package net.andresbustamante.yafoot.web.rs;

import net.andresbustamante.yafoot.model.xs.Utilisateur;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andresbustamante
 */
@RestController
@RequestMapping("/utilisateurs")
public class UtilisateursController {

    @PostMapping(path = "/authentification")
    public void authentifierUtilisateur(@RequestBody Utilisateur utilisateur) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @PostMapping(path = "/recuperationMotDePasse")
    public boolean recupererMdpOublie(@RequestBody String email) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
