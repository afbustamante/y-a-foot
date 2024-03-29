package net.andresbustamante.yafoot.core.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.services.PlayerManagementService;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import net.andresbustamante.yafoot.core.web.mappers.PlayerMapper;
import net.andresbustamante.yafoot.web.dto.Player;
import net.andresbustamante.yafoot.web.dto.PlayerForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {PlayersController.class, ObjectMapper.class},
        properties = {
                "api.config.public.url=http://myurl"
        },
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class PlayersControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlayerManagementService playerManagementService;

    @MockBean
    private PlayerSearchService playerSearchService;

    @MockBean
    private PlayerMapper playerMapper;

    @Test
    void findExistingPlayer() throws Exception {
        // Given
        String email = VALID_EMAIL;

        net.andresbustamante.yafoot.core.model.Player player = new net.andresbustamante.yafoot.core.model.Player();
        player.setFirstName("Test");
        player.setSurname("User");
        player.setEmail(email);
        player.setPhoneNumber("0123456789");

        given(playerSearchService.findPlayerByEmail(anyString(), any(UserContext.class))).willReturn(player);

        // When
        mvc.perform(get("/players?email={0}", email)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findUnknownPlayer() throws Exception {
        // Given
        given(playerSearchService.findPlayerByEmail(anyString(), any(UserContext.class))).willReturn(null);

        // When
        mvc.perform(get("/players?email={0}", VALID_EMAIL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk()).andExpect(content().string("[]"));
    }

    @Test
    void findPlayerWhileDatabaseIsUnavailable() throws Exception {
        // Given
        given(playerSearchService.findPlayerByEmail(anyString(), any(UserContext.class)))
                .willThrow(DatabaseException.class);

        // When
        mvc.perform(get("/players?email={0}", VALID_EMAIL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateExistingPlayer() throws Exception {
        // Given
        PlayerForm player = new PlayerForm();
        player.setFirstName("Test");
        player.setSurname("User");
        player.setPhoneNumber("0123456789");

        net.andresbustamante.yafoot.core.model.Player storedPlayer =
                new net.andresbustamante.yafoot.core.model.Player(1);
        storedPlayer.setEmail(VALID_EMAIL);
        storedPlayer.setFirstName("John");
        storedPlayer.setSurname("Doe");

        net.andresbustamante.yafoot.core.model.Player updatedPlayer =
                new net.andresbustamante.yafoot.core.model.Player();
        updatedPlayer.setFirstName("Test");
        updatedPlayer.setSurname("User");
        updatedPlayer.setPhoneNumber("0123456789");

        given(playerSearchService.findPlayerById(anyInt())).willReturn(storedPlayer);
        given(playerMapper.map(any(PlayerForm.class))).willReturn(updatedPlayer);

        // When
        mvc.perform(put("/players/{0}", 1)
                .content(objectMapper.writeValueAsString(player))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isAccepted());
    }

    @Test
    void updateUnknownPlayer() throws Exception {
        // Given
        PlayerForm player = new PlayerForm();
        player.setFirstName("Test");
        player.setSurname("User");
        player.setPhoneNumber("0123456789");

        net.andresbustamante.yafoot.core.model.Player storedPlayer =
                new net.andresbustamante.yafoot.core.model.Player(1);
        storedPlayer.setEmail(VALID_EMAIL);
        storedPlayer.setFirstName("John");
        storedPlayer.setSurname("Doe");

        given(playerSearchService.findPlayerById(anyInt())).willReturn(null);

        // When
        mvc.perform(put("/players/{0}", 1)
                .content(objectMapper.writeValueAsString(player))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePlayerWhileDatabaseIsUnavailable() throws Exception {
        // Given
        String email = VALID_EMAIL;

        Player player = new Player();
        player.setId(1);
        player.setFirstName("Test");
        player.setSurname("User");
        player.setEmail(email);
        player.setPhoneNumber("0123456789");

        net.andresbustamante.yafoot.core.model.Player storedPlayer =
                new net.andresbustamante.yafoot.core.model.Player(1);
        storedPlayer.setEmail(email);
        storedPlayer.setFirstName("John");
        storedPlayer.setSurname("Doe");

        given(playerSearchService.findPlayerById(anyInt())).willThrow(DatabaseException.class);

        // When
        mvc.perform(put("/players/{0}", 1)
                .content(objectMapper.writeValueAsString(player))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deactivateExistingPlayer() throws Exception {
        // Given
        net.andresbustamante.yafoot.core.model.Player storedPlayer =
                new net.andresbustamante.yafoot.core.model.Player(1);
        storedPlayer.setEmail(VALID_EMAIL);
        storedPlayer.setFirstName("John");
        storedPlayer.setSurname("Doe");

        given(playerSearchService.findPlayerById(anyInt())).willReturn(storedPlayer);

        // When
        mvc.perform(delete("/players/{0}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNoContent());
    }

    @Test
    void deactivateUnknownPlayer() throws Exception {
        // Given
        given(playerSearchService.findPlayerById(anyInt())).willReturn(null);

        // When
        mvc.perform(delete("/players/{0}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void deactivatePlayerWhileDatabaseIsUnavailable() throws Exception {
        // Given
        given(playerSearchService.findPlayerById(anyInt())).willThrow(DatabaseException.class);

        // When
        mvc.perform(delete("/players/{0}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

}
