package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.model.xs.Voiture;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;

import java.util.ArrayList;
import java.util.List;

import static net.andresbustamante.yafoot.web.util.WebConstants.VOITURES;

public class CarRegisterViewModel extends AbstractViewModel {

    private List<Voiture> voitures;
    private Voiture newCar;

    @Init
    @Override
    public void init() {
        newCar = new Voiture();

        if (Executions.getCurrent().getSession().hasAttribute(VOITURES)) {
            voitures = (List<Voiture>) Executions.getCurrent().getSession().getAttribute(VOITURES);
        }

        if (voitures == null) {
            voitures = new ArrayList<>();
        }
    }

    @Command
    public void addCar() {
        voitures.add(newCar);
        Executions.getCurrent().getSession().setAttribute(VOITURES, voitures);
    }

    public Voiture getNewCar() {
        return newCar;
    }
}
