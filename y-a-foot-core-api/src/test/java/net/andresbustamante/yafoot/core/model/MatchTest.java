package net.andresbustamante.yafoot.core.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    @Test
    void isPlayerRegistered() {
        // Given
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        Match match1 = new Match(1);

        Registration reg1 = new Registration(new RegistrationId(match1.getId(), player1.getId()));
        reg1.setPlayer(player1);
        Registration reg2 = new Registration(new RegistrationId(match1.getId(), player2.getId()));
        reg2.setPlayer(player2);

        match1.setRegistrations(List.of(reg1, reg2));

        // When
        var result = match1.isPlayerRegistered(player1);

        // Then
        assertTrue(result);
    }

    @Test
    void isPlayerNotRegistered() {
        // Given
        final Player player1 = new Player(1);
        final Player player2 = new Player(2);
        final Player player3 = new Player(3);

        final Match match1 = new Match(1);

        final Registration reg1 = new Registration(new RegistrationId(match1.getId(), player1.getId()));
        reg1.setPlayer(player1);
        final Registration reg2 = new Registration(new RegistrationId(match1.getId(), player2.getId()));
        reg2.setPlayer(player2);

        match1.setRegistrations(List.of(reg1, reg2));

        // When
        var result = match1.isPlayerRegistered(player3);

        // Then
        assertFalse(result);
    }

    @Test
    void isPlayerNotRegisteredNewMatch() {
        // Given
        final Player player1 = new Player(1);
        final Match match1 = new Match(1);

        // When
        var result = match1.isPlayerRegistered(player1);

        // Then
        assertFalse(result);
    }

    @Test
    void isAcceptingRegistrations() {
        // Given
        final Player player1 = new Player(1);
        final Player player2 = new Player(2);

        final Match match1 = new Match(1);
        match1.setNumPlayersMax(5);
        match1.setDate(LocalDateTime.now().plusDays(1));

        final Registration reg1 = new Registration(new RegistrationId(match1.getId(), player1.getId()));
        reg1.setPlayer(player1);
        final Registration reg2 = new Registration(new RegistrationId(match1.getId(), player2.getId()));
        reg2.setPlayer(player2);

        match1.setRegistrations(List.of(reg1, reg2));

        // When
        var result = match1.isAcceptingRegistrations();

        // Then
        assertTrue(result);
    }

    @Test
    void isNotAcceptingRegistrationsForFullMatch() {
        // Given
        final Player player1 = new Player(1);
        final Player player2 = new Player(2);

        final Match match1 = new Match(1);
        match1.setNumPlayersMax(2);
        match1.setDate(LocalDateTime.now().plusDays(1));

        final Registration reg1 = new Registration(new RegistrationId(match1.getId(), player1.getId()));
        reg1.setPlayer(player1);
        final Registration reg2 = new Registration(new RegistrationId(match1.getId(), player2.getId()));
        reg2.setPlayer(player2);

        match1.setRegistrations(List.of(reg1, reg2));

        // When
        var result = match1.isAcceptingRegistrations();

        // Then
        assertFalse(result);
    }

    @Test
    void isNotAcceptingRegistrationsForPastMatch() {
        // Given
        final Player player1 = new Player(1);
        final Player player2 = new Player(2);

        final Match match1 = new Match(1);
        match1.setNumPlayersMax(5);
        match1.setDate(LocalDateTime.now().minusHours(1));

        final Registration reg1 = new Registration(new RegistrationId(match1.getId(), player1.getId()));
        reg1.setPlayer(player1);
        final Registration reg2 = new Registration(new RegistrationId(match1.getId(), player2.getId()));
        reg2.setPlayer(player2);

        match1.setRegistrations(List.of(reg1, reg2));

        // When
        var result = match1.isAcceptingRegistrations();

        // Then
        assertFalse(result);
    }

    @Test
    void getNumRegisteredPlayers() {
        // Given
        final Player player1 = new Player(1);
        final Player player2 = new Player(2);

        final Match match1 = new Match(1);

        final Registration reg1 = new Registration(new RegistrationId(match1.getId(), player1.getId()));
        reg1.setPlayer(player1);
        final Registration reg2 = new Registration(new RegistrationId(match1.getId(), player2.getId()));
        reg2.setPlayer(player2);

        match1.setRegistrations(List.of(reg1, reg2));

        // When
        var result = match1.getNumRegisteredPlayers();

        // Then
        assertEquals(2, result);
    }

    @Test
    void getNumRegisteredPlayersNewMatch() {
        // Given
        final Match match1 = new Match(1);

        // When
        var result = match1.getNumRegisteredPlayers();

        // Then
        assertEquals(0, result);
    }
}
