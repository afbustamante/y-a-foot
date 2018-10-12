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
import java.util.Collections;
import java.util.List;

/**
 * @author andresbustamante
 */
@ManagedBean
@RequestScoped
public class MatchListBean implements Serializable {

    private List<Match> matches;

    @Inject
    private RechercheMatchsUIService rechercheMatchsUIService;

    private transient final Log log = LogFactory.getLog(MatchListBean.class);

    public MatchListBean() {
    }

    public List<Match> getMatches() {
        if (matches == null) {
            Integer idJoueur = rechercheMatchsUIService.getContexte().getUtilisateur().getId();
            matches = rechercheMatchsUIService.getMatchsJoueur(String.valueOf(idJoueur)).getMatch();
            Collections.sort(matches, new MatchComparator());

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
