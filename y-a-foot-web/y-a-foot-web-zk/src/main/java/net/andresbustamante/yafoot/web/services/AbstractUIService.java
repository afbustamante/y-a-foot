package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Contexte;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.web.util.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import java.text.MessageFormat;
import java.util.Collections;

import static net.andresbustamante.yafoot.model.Contexte.TZ;
import static net.andresbustamante.yafoot.model.Contexte.UTILISATEUR;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * Service abstrait à étendre par tous les services utilisés depuis la couche UI
 *
 * @author andresbustamante
 */
public abstract class AbstractUIService {

    @Value("${api.rest.services.url}")
    protected String backendServicesUrl;

    @Value("${api.rest.joueurs.services.path}")
    private String joueursServicesPath;

    @Value("${api.rest.joueurs.services.email.path}")
    private String joueurParEmailServicesPath;

    private final Logger log = LoggerFactory.getLogger(AbstractUIService.class);
    private Contexte contexte;

    /**
     * Récupérer les informations du contexte de l'utilisateur
     *
     * @return
     * @throws ApplicationException Si un problème parvient lors de la récupération des infos du joueur depuis le serveur
     */
    protected Contexte getContexte() throws ApplicationException {
        if (contexte == null) {
            Session session = Sessions.getCurrent();
            Object obj = session.getAttribute(WebConstants.CONTEXTE);

            if (obj != null) {
                contexte = (Contexte) obj;
            } else {
                SecurityContext securityContext = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
                UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
                String email = userDetails.getUsername();

                Joueur joueur = chercherJoueur(email);

                if (joueur != null) {
                    contexte = new Contexte();
                    contexte.setUtilisateur(joueur);
                    session.setAttribute(WebConstants.CONTEXTE, contexte);
                }
            }
        }
        return contexte;
    }

    /**
     *
     * @return
     * @throws ApplicationException
     */
    protected MultiValueMap<String, String> getHeadersMap() throws ApplicationException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(UTILISATEUR, Collections.singletonList(getContexte().getUtilisateur().getId().toString()));
        headers.put(TZ, Collections.singletonList("CET")); // TODO Injecter la timezone à partir de la session
        return headers;
    }

    /**
     * Chercher les informations du joueur connecté à partir de son adresse mail
     *
     * @param email Adresse mail du joueur
     * @return Informations du joueur
     * @throws ApplicationException Si un problème parvient lors des échanges avec le serveur
     */
    private Joueur chercherJoueur(String email) throws ApplicationException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServicesUrl)
                    .path(joueursServicesPath)
                    .path(MessageFormat.format(joueurParEmailServicesPath, email));
            ResponseEntity<Joueur> response = restTemplate.getForEntity(builder.toUriString(), Joueur.class);
            return response.getBody();
        } catch (RestClientException e) {
            String message = "Erreur lors de la récupération des informations d'un joueur";
            log.error(message, e);
            throw new ApplicationException(message);
        }
    }
}
