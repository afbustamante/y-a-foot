package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Joueur;
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

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

@Component
@SessionScope
public class GestionProfilJoueurUIService extends AbstractUIService {

    @Value("${api.rest.joueurs.services.path}")
    private String joueursServicesPath;

    @Value("${api.rest.joueurs.services.email.path}")
    private String joueurParEmailServicesPath;

    private final Logger log = LoggerFactory.getLogger(GestionProfilJoueurUIService.class);

    public Joueur chargerProfilJoueurActuel() throws ApplicationException {
        return (Joueur) getContexte().getUtilisateur();
    }

    public boolean actualiserDonneesJoueur(Joueur joueur) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(joueursServicesPath)
                    .path(MessageFormat.format(joueurParEmailServicesPath, joueur.getEmail()));

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

    public boolean desactiverJoueur(Joueur joueur) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(joueursServicesPath)
                    .path(MessageFormat.format(joueurParEmailServicesPath, joueur.getEmail()));

            MultiValueMap<String, String> headers = getHeadersMap();
            HttpEntity<Joueur> params = new HttpEntity<>(headers);

            ResponseEntity<Boolean> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.DELETE, params, Boolean.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour des données de l'utilisateur " + joueur.getEmail(), e);
            throw new ApplicationException("Erreur lors de la mise à jour des données");
        }
    }

    public boolean actualiserMotDePasseJoueur(String emailJoueur, String motDePasse) throws ApplicationException {

        Joueur joueur = new Joueur();
        joueur.setEmail(emailJoueur);
        joueur.setMotDePasse(motDePasse.getBytes(StandardCharsets.UTF_8));

        return actualiserDonneesJoueur(joueur);
    }
}
