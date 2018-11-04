package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Joueur;
import net.andresbustamante.yafoot.uiservices.InscriptionJoueursUIService;
import net.andresbustamante.yafoot.util.ConstantesWeb;
import net.andresbustamante.yafoot.util.MessagesProperties;
import net.andresbustamante.yafoot.util.SecuriteUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class SignInBean implements Serializable {

    private String prenom;
    private String nom;
    private String email;
    private String motDePasse1;
    private String motDePasse2;

    private final Log log = LogFactory.getLog(SignInBean.class);

    @Inject
    private InscriptionJoueursUIService inscriptionJoueursUIService;

    /**
     * Déclencher la création d'un nouveau compte de joueur à partir des informations
     * du formulaire
     */
    public String creerNouveauJoueur() {
        try {
            if (motDePasse1.equals(motDePasse2)) {
                Joueur nouveauJoueur = new Joueur();
                nouveauJoueur.setNom(nom);
                nouveauJoueur.setPrenom(prenom);
                nouveauJoueur.setEmail(email);
                nouveauJoueur.setMotDePasse(SecuriteUtils.crypterMotDePasse(motDePasse1));

                boolean succes = inscriptionJoueursUIService.creerNouveauCompteJoueur(nouveauJoueur);

                if (succes) {
                    FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
                            MessagesProperties.getValue("sign.in.successful",
                                    FacesContext.getCurrentInstance().getExternalContext().getRequestLocale()), null);
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                    return ConstantesWeb.SUCCES;
                } else {
                    return ConstantesWeb.ECHEC;
                }
            } else {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        MessagesProperties.getValue("sign.in.password.confirmation.does.not.match",
                                FacesContext.getCurrentInstance().getExternalContext().getRequestLocale()), null);
                FacesContext.getCurrentInstance().addMessage("password2", facesMessage);
                return ConstantesWeb.ECHEC;
            }
        } catch (ApplicationException e) {
            log.error("Erreur lors de la création d'un joueur", e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(e.getMessage(), facesMessage);
            return null;
        }
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse1() {
        return motDePasse1;
    }

    public void setMotDePasse1(String motDePasse1) {
        this.motDePasse1 = motDePasse1;
    }

    public String getMotDePasse2() {
        return motDePasse2;
    }

    public void setMotDePasse2(String motDePasse2) {
        this.motDePasse2 = motDePasse2;
    }
}
