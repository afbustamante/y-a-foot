package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.services.AbstractServiceUnitTest;
import net.andresbustamante.yafoot.commons.utils.TemplateUtils;
import net.andresbustamante.yafoot.core.dao.CarDao;
import net.andresbustamante.yafoot.core.dao.MatchDao;
import net.andresbustamante.yafoot.core.dao.PlayerDao;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.messaging.services.MessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CarpoolingNotificationsServiceImpl}.
 *
 * @see net.andresbustamante.yafoot.core.services.CarpoolingNotificationsService
 */
class CarpoolingNotificationsServiceTest extends AbstractServiceUnitTest {

    @InjectMocks
    private CarpoolingNotificationsServiceImpl carpoolingNotificationsService;

    @Mock
    private MatchDao matchDao;

    @Mock
    private PlayerDao playerDao;

    @Mock
    private CarDao carDao;

    @Mock
    private MessageSource messageSource;

    @Mock
    private TemplateUtils templateUtils;

    @Mock
    private MessagingService messagingService;

    @BeforeEach
    void setUp() throws Exception {
        ReflectionTestUtils.setField(carpoolingNotificationsService, "carpoolingManagementUrl",
                "http://localhost/test/api");
        ReflectionTestUtils.setField(carpoolingNotificationsService, "matchManagementUrl",
                "http://localhost/test/api");
    }

    @Test
    void testNotifyCarpoolingRequest() throws Exception {
        // Given
        int playerId = 1;
        int matchId = 2;
        int carId = 3;

        Player player = new Player(playerId);
        player.setFirstName("player");
        player.setEmail("player@email.com");
        player.setPreferredLanguage("es");

        Match match = new Match(matchId);
        match.setCode("code");
        match.setDate(LocalDateTime.now().plusDays(1));

        Player driver = new Player(5);
        driver.setFirstName("driver");
        driver.setEmail("driver@email.com");
        driver.setPreferredLanguage("fr");

        Car car = new Car(carId);
        car.setName("car");
        car.setDriver(driver);

        when(playerDao.findPlayerById(anyInt())).thenReturn(player);
        when(matchDao.findMatchById(anyInt())).thenReturn(match);
        when(carDao.findCarById(anyInt())).thenReturn(car);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("message");
        when(templateUtils.getContent(anyString(), any())).thenReturn("content");

        // When
        assertDoesNotThrow(() -> carpoolingNotificationsService.notifyCarpoolingRequest(playerId, matchId, carId));

        // Then
        verify(messagingService).sendEmail("driver@email.com", "message", "content");
    }

    @Test
    void testNotifyCarpoolingUpdate() throws Exception {
        // Given
        int playerId = 1;
        int matchId = 2;
        int carId = 3;

        Player player = new Player(playerId);
        player.setFirstName("player");
        player.setEmail("player@email.com");
        player.setPreferredLanguage("es");

        Match match = new Match(matchId);
        match.setCode("code");
        match.setDate(LocalDateTime.now().plusDays(1));

        Player driver = new Player(5);
        driver.setFirstName("driver");
        driver.setEmail("driver@email.com");
        driver.setPreferredLanguage("fr");

        Car car = new Car(carId);
        car.setName("car");
        car.setDriver(driver);

        when(playerDao.findPlayerById(anyInt())).thenReturn(player);
        when(matchDao.findMatchById(anyInt())).thenReturn(match);
        when(carDao.findCarById(anyInt())).thenReturn(car);
        when(messageSource.getMessage(anyString(), eq(null), any(Locale.class))).thenReturn("message");
        when(templateUtils.getContent(anyString(), any())).thenReturn("content");

        // When
        assertDoesNotThrow(() -> carpoolingNotificationsService.notifyCarpoolingUpdate(playerId, matchId, carId,
                true));

        // Then
        verify(messagingService).sendEmail("player@email.com", "message", "content");
    }
}
