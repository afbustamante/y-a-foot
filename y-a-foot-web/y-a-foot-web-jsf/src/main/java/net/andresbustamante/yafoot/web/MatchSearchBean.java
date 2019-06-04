package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Inscription;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.uiservices.InscriptionMatchsUIService;
import net.andresbustamante.yafoot.uiservices.RechercheMatchsUIService;
import net.andresbustamante.yafoot.util.WebConstants;
import net.andresbustamante.yafoot.util.DateUtils;
import net.andresbustamante.yafoot.util.MessagesProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.security.RolesAllowed;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Locale;

import static net.andresbustamante.yafoot.security.Roles.JOUEUR;

/**
 * @author andresbustamante
 */
@ManagedBean
@ViewScoped
public class MatchSearchBean extends AbstractFacesBean implements Serializable {

    private String codeMatch;
    private Match match;
    private String complementJoueurs;
    private Locale locale;
    private String patternDate;
    private final transient Log log = LogFactory.getLog(MatchSearchBean.class);
    private boolean inscriptionPossible = false;
    private boolean validationRequise = false;
    private String optionInscription;

    @Inject
    private RechercheMatchsUIService rechercheMatchsUIService;

    @Inject
    private InscriptionMatchsUIService inscriptionMatchsUIService;

    public MatchSearchBean() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    @RolesAllowed(JOUEUR)
    public void chercherMatch() {
        log.info("Recherche du match avec le code " + codeMatch);
        match = rechercheMatchsUIService.chercherMatchParCode(codeMatch);
    }

    public void afficherDialogValidation(ActionEvent event) {
        validationRequise = true;
    }

    public void fermerDialogValidation(ActionEvent event) {
        validationRequise = false;
    }

    @RolesAllowed(JOUEUR)
    public String validerInscription() {
        fermerDialogValidation(null);

        if (optionInscription != null) {
            try {
                switch (optionInscription) {
                    case "OK_WITH_CAR":
                        // TODO Implémenter le service de mise d'inscription avec voiture
                    case "OK_WITHOUT_CAR":
                        // TODO Implémenter le service d'orientation vers les joueurs avec voitures
                    case "OK_OTHER":
                        boolean inscrit = inscriptionMatchsUIService.inscrireJoueurMatch(match,null);

                        if (inscrit) {
                            ajouterMessageInfo("match.join.success.summary.text", "match.join.success.detail.text", null);
                            return WebConstants.SUCCES;
                        } else {
                            ajouterMessageErreur("match.join.error.summary.text", "match.join.error.detail.text", null);
                            return WebConstants.ECHEC;
                        }
                    default:
                        return WebConstants.ECHEC;
                }
            } catch (ApplicationException e) {
                // Afficher l'exception
                log.error("Erreur lors de l'inscription : " + e.getCause());
                return WebConstants.ECHEC;
            }
        } else {
            log.error("Pas d'option valide pour finaliser l'inscription");
            return WebConstants.ECHEC;
        }
    }

    public String getCodeMatch() {
        return codeMatch;
    }

    public void setCodeMatch(String codeMatch) {
        this.codeMatch = codeMatch;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public String getComplementJoueurs() {
        Integer numPlacesDisponibles = getNumPlacesDisponibles();

        if (numPlacesDisponibles != null)
            complementJoueurs = MessagesProperties.getValue("match.search.result.spots.left", locale,
                    getNumPlacesDisponibles());
        return complementJoueurs;
    }

    public void setComplementJoueurs(String complementJoueurs) {
        this.complementJoueurs = complementJoueurs;
    }

    private Integer getNumPlacesDisponibles() {
        if ((match != null) && (match.getNbJoueursMax() != null) && (match.getInscriptions() != null)) {
            return match.getNbJoueursMax() - match.getInscriptions().getInscription().size();
        }
        return null;
    }

    /**
     * Vérifie si un joueur est éligible pour s'inscrire à un match
     *
     * @return Booléan indiquant si le match a l'option de partage de code actif et le joueur n'est pas encore inscrit
     */
    public boolean isInscriptionPossible() {
        // L'inscription est possible si le match a l'option de partage de code actif et que le joueur n'est pas encore inscrit
        if (match != null && match.isPartageActif()) {
            inscriptionPossible = true;

            for (Inscription ins : match.getInscriptions().getInscription()) {
                if (ins.getJoueur().getEmail().equals(getNomUtilisateurActif())) {
                    // Le joueur est déjà inscrit au match
                    inscriptionPossible = false;
                }
            }
        } else {
            inscriptionPossible = false;
        }
        return inscriptionPossible;
    }

    public void setInscriptionPossible(boolean inscriptionPossible) {
        this.inscriptionPossible = inscriptionPossible;
    }

    public boolean isValidationRequise() {
        return validationRequise;
    }

    public void setValidationRequise(boolean validationRequise) {
        this.validationRequise = validationRequise;
    }

    public String getOptionInscription() {
        return optionInscription;
    }

    public void setOptionInscription(String optionInscription) {
        this.optionInscription = optionInscription;
    }

    public String getPatternDate() {
        if (patternDate == null) {
            patternDate = DateUtils.getPatternDate(locale.getLanguage());
        }
        return patternDate;
    }

    public void setPatternDate(String patternDate) {
        this.patternDate = patternDate;
    }
}
