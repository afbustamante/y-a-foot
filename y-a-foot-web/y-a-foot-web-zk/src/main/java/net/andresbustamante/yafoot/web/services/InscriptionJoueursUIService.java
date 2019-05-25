package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.util.SecuriteUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@SessionScope
public class InscriptionJoueursUIService extends AbstractUIService {

    @Value("${api.rest.joueurs.services.path}")
    private String joueursServicesPath;

    /**
     * Créer un nouveau joueur dans l'application
     *
     * @param joueur Joueur à créer
     * @throws ApplicationException
     */
    public boolean creerNouveauCompteJoueur(Joueur joueur) throws ApplicationException {
        try {
            crypterMotDePasse(joueur);

            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(joueursServicesPath);

            ResponseEntity<Boolean> response = restTemplate.postForEntity(builder.toUriString(), joueur, Boolean.class);
            boolean succes = (response.getHeaders().getLocation() != null);

            return (response.getStatusCode().is2xxSuccessful() && succes);
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    private void crypterMotDePasse(Joueur joueur) throws ApplicationException {
        String mdp = joueur.getMotDePasse();
        joueur.setMotDePasse(SecuriteUtils.crypterMotDePasse(mdp));
    }
}