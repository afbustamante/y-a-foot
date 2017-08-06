package net.andresbustamante.yafoot.ws;

import net.andresbustamante.framework.web.TransactionalWebService;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.JoueurMatch;
import net.andresbustamante.yafoot.model.Match;
import net.andresbustamante.yafoot.model.Site;
import net.andresbustamante.yafoot.services.GestionMatchsService;
import net.andresbustamante.yafoot.util.ContexteUtils;
import net.andresbustamante.yafoot.util.DateUtils;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.transaction.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author andresbustamante
 */
@WebService(serviceName = "OrganisationMatchsService", portName = "OrganisationMatchsPort",
        endpointInterface = "net.andresbustamante.yafoot.ws.OrganisationMatchsPortType",
        targetNamespace = "http://andresbustamante.net/yafoot/ws",
        wsdlLocation = "WEB-INF/wsdl/organisation_matchs.wsdl")
public class OrganisationMatchsWS extends TransactionalWebService {

    @Resource
    private UserTransaction utx;

    @EJB
    private GestionMatchsService gestionMatchsService;

    public String creerMatch(net.andresbustamante.yafoot.xs.Match match,
                              net.andresbustamante.yafoot.xs.Contexte contexte) throws BDDException {
        try {
            utx.begin();
            Match nouveauMatch = creerMatch(match);
            gestionMatchsService.creerMatch(nouveauMatch, ContexteUtils.copierInfoContexte(contexte));
            String codeMatch = nouveauMatch.getCode();
            utx.commit();
            return codeMatch;
        }  catch (net.andresbustamante.yafoot.exceptions.BDDException e) {
            throw new BDDException(e.getMessage(), e.getMessage());
        } catch (NotSupportedException | SystemException e) {
            rollbackTransaction(utx);
            throw new BDDException("Erreur de BDD au moment de créer la transaction", e.getMessage(), e);
        } catch (HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            rollbackTransaction(utx);
            throw new BDDException("Erreur de BDD au moment de confirmer la transaction", e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            rollbackTransaction(utx);
            throw new IllegalArgumentException(e.getMessage());
        }
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

        match.setSite(copierSite(matchXml.getSite()));

        return match;
    }

    private Site copierSite(net.andresbustamante.yafoot.xs.Site siteXml) {
        if (siteXml != null) {
            Site site = new Site();
            site.setId(siteXml.getId());
            site.setNom(siteXml.getNom());
            site.setAdresse(siteXml.getAdresse());
            site.setTelephone(siteXml.getNumeroTelephone());

            // TODO Traiter les coordonnées GPS
            return site;
        }
        return null;
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
