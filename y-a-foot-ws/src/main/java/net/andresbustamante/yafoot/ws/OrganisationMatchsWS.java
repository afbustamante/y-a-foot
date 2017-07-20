package net.andresbustamante.yafoot.ws;

import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.JoueurMatch;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.util.DateUtils;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.List;

/**
 * @author andresbustamante
 */
@WebService(serviceName = "OrganisationMatchsService", portName = "OrganisationMatchsPort",
        endpointInterface = "net.andresbustamante.yafoot.ws.OrganisationMatchsPortType",
        targetNamespace = "http://andresbustamante.net/yafoot/ws",
        wsdlLocation = "WEB-INF/wsdl/organisation_matchs.wsdl")
public class OrganisationMatchsWS {

    @Resource
    private UserTransaction utx;

    public boolean creerMatch(net.andresbustamante.yafoot.xs.Match match,
                              net.andresbustamante.yafoot.xs.Contexte contexte) throws BDDException {
        Match nouveauMatch = creerMatch(match);
        return false;
    }

    public boolean inscrireJoueurMatch(net.andresbustamante.yafoot.xs.Joueur joueur,
                                       net.andresbustamante.yafoot.xs.Match match,
                                       net.andresbustamante.yafoot.xs.Voiture voiture,
                                       net.andresbustamante.yafoot.xs.Contexte contexte) throws BDDException {
        return false;
    }

    public boolean desinscrireJoueurMatch(net.andresbustamante.yafoot.xs.Joueur joueur,
                                          net.andresbustamante.yafoot.xs.Match match,
                                          net.andresbustamante.yafoot.xs.Contexte contexte) throws BDDException {
        // TODO Implement this method
        return false;
    }

    public boolean annulerMatch(net.andresbustamante.yafoot.xs.Match match,
                                net.andresbustamante.yafoot.xs.Contexte contexte) throws BDDException {
        // TODO Implement this method
        return false;
    }

    private Match creerMatch(net.andresbustamante.yafoot.xs.Match matchXml) {
        Match match = new Match();
        match.setDateMatch(DateUtils.transformer(matchXml.getDate()));
        match.setDescription(matchXml.getDescription());
        match.setNumJoueursMin(matchXml.getNumJoueursMin());
        match.setNumJoueursMax(matchXml.getNumJoueursMax());

        if (matchXml.getJoueurs() != null && CollectionUtils.isNotEmpty(matchXml.getJoueurs().getJoueur())) {
            List<JoueurMatch> listeJoueurs = new ArrayList<>();

            for (net.andresbustamante.yafoot.xs.Joueur joueurXml : matchXml.getJoueurs().getJoueur()) {
                Joueur joueur = copierJoueur(joueurXml);
                JoueurMatch joueurMatch = new JoueurMatch();
                joueurMatch.setJoueur(joueur);
                joueurMatch.setMatch(match);

                // TODO Traiter les voitures
            }
            match.setJoueursMatch(listeJoueurs);
        }
        return match;
    }

    private Joueur copierJoueur(net.andresbustamante.yafoot.xs.Joueur joueurXml) {
        Joueur joueur = new Joueur();
        joueur.setId(joueurXml.getId());
        joueur.setEmail(joueurXml.getEmail());
        joueur.setPrenom(joueurXml.getPrenom());

        if (joueurXml.getVoiture() != null) {
            // TODO Traiter les voitures
        }
        return joueur;
    }

}
