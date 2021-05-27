package net.andresbustamante.yafoot.core.dao;

import net.andresbustamante.yafoot.core.model.Sport;
import net.andresbustamante.yafoot.core.model.enums.SportEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SportDAOTest extends AbstractDAOTest {

    @Autowired
    private SportDAO sportDAO;

    @Test
    void loadSports() {
        // When
        List<Sport> sports = sportDAO.loadSports();

        // Then
        assertNotNull(sports);
        sports.forEach(sport -> {
            assertNotNull(sport.getId());
            assertNotNull(sport.getCode());
            assertNotNull(sport.getName());
        });
        assertTrue(sports.stream().anyMatch(sport -> sport.getCode().equals(SportEnum.FOOTBALL.name())));
        assertTrue(sports.stream().anyMatch(sport -> sport.getCode().equals(SportEnum.RUGBY.name())));
    }
}