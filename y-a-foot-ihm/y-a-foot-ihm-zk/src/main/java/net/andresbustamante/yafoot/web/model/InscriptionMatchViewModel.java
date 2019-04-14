package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.model.enums.ModeInscriptionMatchEnum;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;

public class InscriptionMatchViewModel extends AbstractViewModel {

    private String codeMatch;

    private String modeInscription;

    @Init
    @Override
    public void init() {
        codeMatch = Executions.getCurrent().getParameter("code");
    }

    @Command
    public void choisirMode(@BindingParam("mode") String mode) {
        this.modeInscription = mode;
    }

    @Command
    public void joindreMatch() {
        ModeInscriptionMatchEnum mode = ModeInscriptionMatchEnum.valueOf(modeInscription);

        switch (mode) {
            case INSCRIPTION_AVEC_VOITURE:
                // TODO Implémenter inscription avec voiture
                break;
            case INSCRIPTION_SANS_VOITURE:
                // TODO Implémenter inscription sans voiture
                break;
            case INSCRIPTION_AUTRE_TRANSPORT:
                // TODO Implémenter inscription avec un autre transport
                break;
            default:
                break;
        }
    }

    public String getModeInscription() {
        return modeInscription;
    }

    public void setModeInscription(String modeInscription) {
        this.modeInscription = modeInscription;
    }
}
