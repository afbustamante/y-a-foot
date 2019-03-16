package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.xs.Inscription;
import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.web.services.RechercheMatchsUIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author andresbustamante
 */
public class RechercheMatchController extends AbstractController {

    private final transient Logger log = LoggerFactory.getLogger(RechercheMatchController.class);

    private Match match;

    @Wire
    private Grid grdMatchDetail;

    @Wire
    private Textbox txbCode;

    @Wire
    private Label lblDate;

    @Wire
    private Label lblPlace;

    @Wire
    private Listbox lsbPlayers;

    @WireVariable
    private RechercheMatchsUIService rechercheMatchsUIService;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        grdMatchDetail.setVisible(false);
    }

    @Listen(Events.ON_CLICK + "= #btnSearch")
    public void chercherMatch() {
        String code = txbCode.getValue();

        try {
            Match match = rechercheMatchsUIService.chercherMatchParCode(code);

            if (match != null) {
                Locale locale = getLocaleUtilisateur();
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
                DateFormat heureFormat = new SimpleDateFormat("H:mm z");

                String texteDate = dateFormat.format(match.getDate().getTime()) + NL +
                        heureFormat.format(match.getDate().getTime());

                grdMatchDetail.setVisible(true);
                lblDate.setValue(texteDate);
                lblPlace.setValue(match.getSite().getNom() + NL + match.getSite().getAdresse());

                List<Inscription> inscriptions = (match.getInscriptions().getInscription() != null) ?
                        match.getInscriptions().getInscription() : Collections.emptyList();
                ListModel<Inscription> inscriptionsListModel = new ListModelArray<>(inscriptions);

                lsbPlayers.setModel(inscriptionsListModel);
            } else {
                grdMatchDetail.setVisible(false);
            }
        } catch (ApplicationException e) {
            log.error("Erreur lors de la recherche d'un match", e);
        }
    }
}
