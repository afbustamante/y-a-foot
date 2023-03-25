package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.dao.PlayerDao;
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

    private final PlayerDao playerDAO;

    @Autowired
    public PlayerSearchServiceImpl(PlayerDao playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public Player findPlayerByEmail(String email, UserContext context) throws ApplicationException {
        if (context != null && !context.getUsername().equals(email)) {
            throw new ApplicationException("unauthorised.user.error", "User not allowed to search a different email");
        }

        return playerDAO.findPlayerByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Player findPlayerById(Integer id) throws DatabaseException {
        return playerDAO.findPlayerById(id);
    }
}
