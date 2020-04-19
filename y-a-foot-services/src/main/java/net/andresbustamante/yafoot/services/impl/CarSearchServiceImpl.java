package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Car;
import net.andresbustamante.yafoot.model.Player;
import net.andresbustamante.yafoot.services.CarSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarSearchServiceImpl implements CarSearchService {

    @Autowired
    private CarDAO carDAO;

    @Transactional(readOnly = true)
    @Override
    public List<Car> findCarsByPlayer(Player player) throws DatabaseException {
        return carDAO.findCarsByPlayer(player);
    }
}
