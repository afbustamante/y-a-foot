package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.core.dao.PlayerDAO;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author andresbustamante
 */
@Service
public class PlayerSearchServiceImpl implements PlayerSearchService {

    private PlayerDAO playerDAO;

    @Autowired
    public PlayerSearchServiceImpl(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public Player findPlayerByEmail(String email) throws DatabaseException {
        return playerDAO.findPlayerByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Player findPlayerById(Integer id) throws DatabaseException {
        return playerDAO.findPlayerById(id);
    }
}
