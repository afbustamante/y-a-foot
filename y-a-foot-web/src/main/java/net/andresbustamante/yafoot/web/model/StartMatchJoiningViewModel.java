package net.andresbustamante.yafoot.web.model;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;

import static net.andresbustamante.yafoot.web.util.WebConstants.MATCH_JOIN_MODE;

public class StartMatchJoiningViewModel extends AbstractViewModel {

    @Override
    public void init() {
        // no-op
    }

    @Command
    public void chooseMode(@BindingParam("mode") String mode) {
        Executions.getCurrent().getSession().setAttribute(MATCH_JOIN_MODE, mode);
    }
}
