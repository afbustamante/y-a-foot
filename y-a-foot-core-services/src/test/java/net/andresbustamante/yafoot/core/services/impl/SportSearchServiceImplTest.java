package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.services.AbstractServiceTest;
import net.andresbustamante.yafoot.core.dao.SportDAO;
import net.andresbustamante.yafoot.core.model.Sport;
import net.andresbustamante.yafoot.core.model.enums.SportEnum;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SportSearchServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    private SportSearchServiceImpl sportSearchService;

    @Mock
    private SportDAO sportDAO;

    @Test
    void loadSports() throws Exception {
        // Given
        List<Sport> sports = List.of(
                new Sport(1, SportEnum.FOOTBALL.name()),
                new Sport(2, SportEnum.RUGBY.name())
        );

        // When
        when(sportDAO.loadSports()).thenReturn(sports);
        List<Sport> result = sportSearchService.loadSports();

        // Then
        assertNotNull(result);
        assertEquals(sports.size(), result.size());
        verify(sportDAO).loadSports();
    }
}