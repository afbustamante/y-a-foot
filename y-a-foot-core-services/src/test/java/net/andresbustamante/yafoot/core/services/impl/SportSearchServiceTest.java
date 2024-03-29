package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.services.AbstractServiceUnitTest;
import net.andresbustamante.yafoot.core.dao.SportDao;
import net.andresbustamante.yafoot.core.model.Sport;
import net.andresbustamante.yafoot.core.model.enums.SportEnum;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SportSearchServiceTest extends AbstractServiceUnitTest {

    @InjectMocks
    private SportSearchServiceImpl sportSearchService;

    @Mock
    private SportDao sportDAO;

    @Test
    void loadSports() throws Exception {
        // Given
        List<Sport> sports = List.of(
                new Sport((short) 1, SportEnum.FOOTBALL.name()),
                new Sport((short) 2, SportEnum.RUGBY.name())
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
