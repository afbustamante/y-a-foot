package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.UserContext;
import net.andresbustamante.yafoot.services.PlayerManagementService;
import net.andresbustamante.yafoot.services.PlayerSearchService;
import net.andresbustamante.yafoot.web.dto.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayersController.class)
class PlayersControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlayerManagementService playerManagementService;

    @MockBean
    private PlayerSearchService playerSearchService;

    @Test
    void createNewPlayer() throws Exception {
        // Given
        String email = "test@email.com";

        Player player = new Player();
        player.setFirstName("Test");
        player.setSurname("User");
        player.setEmail(email);
        player.setPhoneNumber("0123456789");
        player.setPassword("test".getBytes());

        given(playerManagementService.savePlayer(any(net.andresbustamante.yafoot.model.Player.class), any(UserContext.class))).willReturn(1);

        // When
        mvc.perform(post("/players")
                .content(objectMapper.writeValueAsString(player))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION));
    }

    @Test
    void createNewPlayerWhileDatabaseIsUnavailable() throws Exception {
        // Given
        String email = "test@email.com";

        Player player = new Player();
        player.setFirstName("Test");
        player.setSurname("User");
        player.setEmail(email);
        player.setPhoneNumber("0123456789");
        player.setPassword("test".getBytes());

        given(playerManagementService.savePlayer(any(net.andresbustamante.yafoot.model.Player.class),
                any(UserContext.class))).willThrow(DatabaseException.class);

        // When
        mvc.perform(post("/players")
                .content(objectMapper.writeValueAsString(player))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findExistingPlayer() throws Exception {
        // Given
        String email = VALID_EMAIL;

        net.andresbustamante.yafoot.model.Player player = new net.andresbustamante.yafoot.model.Player();
        player.setFirstName("Test");
        player.setSurname("User");
        player.setEmail(email);
        player.setPhoneNumber("0123456789");

        given(playerSearchService.findPlayerByEmail(anyString())).willReturn(player);

        // When
        mvc.perform(get("/players?email={0}", email)
                .header(AUTHORIZATION, getAuthString(email))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findUnknownPlayer() throws Exception {
        // Given
        String email = VALID_EMAIL;

        given(playerSearchService.findPlayerByEmail(anyString())).willReturn(null);

        // When
        mvc.perform(get("/players?email={0}", email)
                .header(AUTHORIZATION, getAuthString(email))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void findPlayerWhileDatabaseIsUnavailable() throws Exception {
        // Given
        String email = VALID_EMAIL;

        given(playerSearchService.findPlayerByEmail(anyString())).willThrow(DatabaseException.class);

        // When
        mvc.perform(get("/players?email={0}", email)
                .header(AUTHORIZATION, getAuthString(email))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateExistingPlayer() throws Exception {
        // Given
        String email = VALID_EMAIL;

        Player player = new Player();
        player.setId(1);
        player.setFirstName("Test");
        player.setSurname("User");
        player.setEmail(email);
        player.setPhoneNumber("0123456789");

        net.andresbustamante.yafoot.model.Player storedPlayer = new net.andresbustamante.yafoot.model.Player(1);
        storedPlayer.setEmail(email);
        storedPlayer.setFirstName("John");
        storedPlayer.setSurname("Doe");

        given(playerSearchService.findPlayerById(anyInt())).willReturn(storedPlayer);

        // When
        mvc.perform(put("/players/{0}", 1)
                .header(HttpHeaders.AUTHORIZATION, getAuthString(email))
                .content(objectMapper.writeValueAsString(player))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isAccepted());
    }

    @Test
    void updateUnknownPlayer() throws Exception {
        // Given
        String email = VALID_EMAIL;

        Player player = new Player();
        player.setId(1);
        player.setFirstName("Test");
        player.setSurname("User");
        player.setEmail(email);
        player.setPhoneNumber("0123456789");

        net.andresbustamante.yafoot.model.Player storedPlayer = new net.andresbustamante.yafoot.model.Player(1);
        storedPlayer.setEmail(email);
        storedPlayer.setFirstName("John");
        storedPlayer.setSurname("Doe");

        given(playerSearchService.findPlayerById(anyInt())).willReturn(null);

        // When
        mvc.perform(put("/players/{0}", 1)
                .header(HttpHeaders.AUTHORIZATION, getAuthString(email))
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

        net.andresbustamante.yafoot.model.Player storedPlayer = new net.andresbustamante.yafoot.model.Player(1);
        storedPlayer.setEmail(email);
        storedPlayer.setFirstName("John");
        storedPlayer.setSurname("Doe");

        given(playerSearchService.findPlayerById(anyInt())).willThrow(DatabaseException.class);

        // When
        mvc.perform(put("/players/{0}", 1)
                .header(HttpHeaders.AUTHORIZATION, getAuthString(email))
                .content(objectMapper.writeValueAsString(player))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deactivateExistingPlayer() throws Exception {
        // Given
        String email = VALID_EMAIL;

        net.andresbustamante.yafoot.model.Player storedPlayer = new net.andresbustamante.yafoot.model.Player(1);
        storedPlayer.setEmail(email);
        storedPlayer.setFirstName("John");
        storedPlayer.setSurname("Doe");

        given(playerSearchService.findPlayerById(anyInt())).willReturn(storedPlayer);

        // When
        mvc.perform(delete("/players/{0}", 1)
                .header(HttpHeaders.AUTHORIZATION, getAuthString(email))
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
                .header(HttpHeaders.AUTHORIZATION, getAuthString(VALID_EMAIL))
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
                .header(HttpHeaders.AUTHORIZATION, getAuthString(VALID_EMAIL))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

}