package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.model.xs.Contexte;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.web.util.ConstantesWeb;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import java.text.MessageFormat;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * @author andresbustamante
 */
public abstract class AbstractUIService {

    private Contexte contexte;

    protected Contexte getContexte() {
        if (contexte == null) {
            Session session = Sessions.getCurrent();
            Object obj = session.getAttribute(ConstantesWeb.CONTEXTE);

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
                    session.setAttribute(ConstantesWeb.CONTEXTE, contexte);
                }
            }
        }
        return contexte;
    }

    protected abstract String getServerUrl();

    protected abstract String getJoueursPath();

    private Joueur chercherJoueur(String email) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Joueur> response = restTemplate.getForEntity(getServerUrl() + getJoueursPath() + MessageFormat.format("/{0}/email", email), Joueur.class);
        return response.getBody();
    }
}
