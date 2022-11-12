package net.andresbustamante.yafoot.core.web.controllers;

import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.model.Sport;
import net.andresbustamante.yafoot.core.model.enums.SportEnum;
import net.andresbustamante.yafoot.core.services.SportSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SportsController.class)
@Import(SportsController.class)
class SportsControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SportSearchService sportSearchService;

    @Test
    void loadSportsOk() throws Exception {
        // Given
        List<Sport> sports = List.of(
                new Sport((short) 1, SportEnum.FOOTBALL.name()),
                new Sport((short) 2, SportEnum.RUGBY.name())
        );
        given(sportSearchService.loadSports()).willReturn(sports);

        // When
        mvc.perform(get("/sports")
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void loadSportsKo() throws Exception {
        // Given
        given(sportSearchService.loadSports()).willThrow(DatabaseException.class);

        // When
        mvc.perform(get("/sports")
                .header(AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }
}
