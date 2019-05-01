package net.andresbustamante.yafoot.web.services;

import net.andresbustamante.yafoot.model.xs.Joueur;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class GestionProfilJoueurUIService extends AbstractUIService {

    @Value("${backend.rest.services.uri}")
    private String restServerUrl;

    @Value("${gestion.joueurs.service.path}")
    private String gestionJoueursPath;

    public Joueur chargerProfilJoueurActuel() {
        return (Joueur) getContexte().getUtilisateur();
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
