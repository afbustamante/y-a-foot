package net.andresbustamante.yafoot.ws;

import net.andresbustamante.framework.web.TransactionalWebService;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Voiture;
import net.andresbustamante.yafoot.services.GestionJoueursService;
import net.andresbustamante.yafoot.util.ContexteUtils;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.transaction.*;
import java.util.Arrays;

/**
 * @author andresbustamante
 */
@WebService(serviceName = "InscriptionJoueursService", portName = "InscriptionJoueursPort",
        endpointInterface = "net.andresbustamante.yafoot.ws.InscriptionJoueursPortType",
        targetNamespace = "http://andresbustamante.net/yafoot/ws",
        wsdlLocation = "WEB-INF/wsdl/inscription_joueurs.wsdl")
public class InscriptionJoueursWS extends TransactionalWebService {

    @EJB
    private GestionJoueursService gestionJoueursService;

    @Resource
    private UserTransaction utx;

    /**
     *
     * @param joueur
     * @param contexte
     * @return
     * @throws BDDException
     */
    public boolean inscrireJoueur(net.andresbustamante.yafoot.xs.Joueur joueur,
                                  net.andresbustamante.yafoot.xs.Contexte contexte)
            throws BDDException {
        boolean inscrit;

        try {
            utx.begin();
            net.andresbustamante.yafoot.model.Joueur nouveauJoueur = gestionJoueursService.inscrireJoueur(creerJoueur
                            (joueur),
                    ContexteUtils.copierInfoContexte(contexte));
            utx.commit();
            inscrit = (nouveauJoueur != null && nouveauJoueur.getId() != null);
        } catch (net.andresbustamante.yafoot.exceptions.BDDException e) {
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
        return inscrit;
    }

    /**
     *
     * @param joueur
     * @param contexte
     * @return
     * @throws BDDException
     */
    public boolean actualiserJoueur(net.andresbustamante.yafoot.xs.Joueur joueur,
                                    net.andresbustamante.yafoot.xs.Contexte contexte)
            throws BDDException {
        // TODO Implement this method
        return false;
    }

    /**
     *
     * @param joueurXml
     * @return
     */
    private Joueur creerJoueur(net.andresbustamante.yafoot.xs.Joueur joueurXml) {
        Joueur joueur = new Joueur();
        joueur.setEmail(joueurXml.getEmail());
        joueur.setMotDePasse(joueurXml.getMotDePasse());
        joueur.setNom(joueurXml.getNom());
        joueur.setPrenom(joueurXml.getPrenom());
        joueur.setTelephone(joueurXml.getNumeroTelephone());

        if (joueurXml.getVoiture() != null) {
            Voiture voiture = creerVoiture(joueurXml.getVoiture());
            voiture.setChauffeur(joueur);
            joueur.setVoitures(Arrays.asList(voiture));
        }
        return joueur;
    }

    /**
     *
     * @param voitureXml
     * @return
     */
    private Voiture creerVoiture(net.andresbustamante.yafoot.xs.Voiture voitureXml) {
        Voiture voiture = new Voiture();
        voiture.setNom(voitureXml.getNom());
        voiture.setNumPlaces(voitureXml.getNumPlaces());
        return voiture;
    }
}
