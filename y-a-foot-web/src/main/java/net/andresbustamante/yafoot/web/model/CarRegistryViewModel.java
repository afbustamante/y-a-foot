package net.andresbustamante.yafoot.web.model;

import net.andresbustamante.yafoot.model.xs.Car;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;

import java.util.ArrayList;
import java.util.List;

import static net.andresbustamante.yafoot.web.util.WebConstants.VOITURES;

public class CarRegistryViewModel extends AbstractViewModel {

    private List<Car> cars;
    private Car newCar;

    @Init
    @Override
    public void init() {
        newCar = new Car();

        if (Executions.getCurrent().getSession().hasAttribute(VOITURES)) {
            cars = (List<Car>) Executions.getCurrent().getSession().getAttribute(VOITURES);
        }

        if (cars == null) {
            cars = new ArrayList<>();
        }
    }

    @Command
    public void addCar() {
        cars.add(newCar);
        Executions.getCurrent().getSession().setAttribute(VOITURES, cars);
    }

    public Car getNewCar() {
        return newCar;
    }
}
