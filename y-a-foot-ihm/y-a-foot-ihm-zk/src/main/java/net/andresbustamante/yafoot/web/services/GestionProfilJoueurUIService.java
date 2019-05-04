package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.util.SecuriteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@SessionScope
public class GestionProfilJoueurUIService extends AbstractUIService {

    @Value("${backend.rest.services.uri}")
    private String restServerUrl;

    @Value("${gestion.joueurs.service.path}")
    private String gestionJoueursPath;

    private final Logger log = LoggerFactory.getLogger(GestionProfilJoueurUIService.class);

    public Joueur chargerProfilJoueurActuel() throws ApplicationException {
        return (Joueur) getContexte().getUtilisateur();
    }

    public boolean actualiserDonneesJoueur(Joueur joueur) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(restServerUrl + gestionJoueursPath);
            builder.path("/").path(joueur.getEmail()).path("/email");

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Joueur> params = new HttpEntity<>(joueur, headers);

            ResponseEntity<Boolean> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.PUT, params, Boolean.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour des données de l'utilisateur " + joueur.getEmail(), e);
            throw new ApplicationException("Erreur lors de la mise à jour des données");
        }
    }

    public String crypterMotDePasse(String mdp) throws ApplicationException {
        return SecuriteUtils.crypterMotDePasse(mdp);
    }

    public boolean actualiserMotDePasseJoueur(String emailJoueur, String motDePasse) throws ApplicationException {
        String mdpCrypte = crypterMotDePasse(motDePasse);

        Joueur joueur = new Joueur();
        joueur.setEmail(emailJoueur);
        joueur.setMotDePasse(mdpCrypte);

        return actualiserDonneesJoueur(joueur);
    }

    @Override
    protected String getServerUrl() {
        return restServerUrl;
    }

    @Override
    protected String getJoueursPath() {
        return gestionJoueursPath;
    }
}
