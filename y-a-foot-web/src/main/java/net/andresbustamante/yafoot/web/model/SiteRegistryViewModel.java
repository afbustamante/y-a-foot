package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.web.dto.Site;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;

import java.util.ArrayList;
import java.util.List;

import static net.andresbustamante.yafoot.web.util.WebConstants.SITES;

/**
 * @author andresbustamante
 */
public class SiteRegistryViewModel extends AbstractViewModel {

    private List<Site> sites;

    private Site newSite;

    @Init
    @Override
    public void init() {
        newSite = new Site();

        if (Executions.getCurrent().getSession().hasAttribute(SITES)) {
            sites = (List<Site>) Executions.getCurrent().getSession().getAttribute(SITES);
        }

        if (sites == null) {
            sites = new ArrayList<>();
        }
    }

    @Command
    public void addSite() {
        sites.add(newSite);
        Executions.getCurrent().getSession().setAttribute(SITES, sites);
    }

    public Site getNewSite() {
        return newSite;
    }
}
