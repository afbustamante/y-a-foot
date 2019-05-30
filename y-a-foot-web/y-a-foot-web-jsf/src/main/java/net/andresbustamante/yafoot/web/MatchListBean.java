package net.andresbustamante.yafoot.web;

import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.uiservices.RechercheMatchsUIService;
import net.andresbustamante.yafoot.util.MatchComparator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

/**
 * @author andresbustamante
 */
@ManagedBean
@RequestScoped
public class MatchListBean extends AbstractFacesBean implements Serializable {

    private List<Match> matches;

    @Inject
    private RechercheMatchsUIService rechercheMatchsUIService;

    private final transient Log log = LogFactory.getLog(MatchListBean.class);

    public List<Match> getMatches() {
        if (matches == null) {
            Integer idJoueur = rechercheMatchsUIService.getContexte().getUtilisateur().getId();
            matches = rechercheMatchsUIService.chercherMatchsJoueur(String.valueOf(idJoueur)).getMatch();
            matches.sort(new MatchComparator());

            if (CollectionUtils.isNotEmpty(matches)) {
                log.info(matches.size() + " matches trouvés pour le joueur " + idJoueur);
            }
        }
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
