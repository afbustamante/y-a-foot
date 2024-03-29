package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.commons.services.AbstractServiceUnitTest;
import net.andresbustamante.yafoot.core.dao.CarDao;
import net.andresbustamante.yafoot.core.dao.MatchDao;
import net.andresbustamante.yafoot.core.events.CarpoolingRequestEvent;
import net.andresbustamante.yafoot.core.events.CarpoolingUpdateEvent;
import net.andresbustamante.yafoot.core.exceptions.UnauthorisedUserException;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Player;
import net.andresbustamante.yafoot.core.model.Registration;
import net.andresbustamante.yafoot.core.model.RegistrationId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarpoolingServiceTest extends AbstractServiceUnitTest {

    @InjectMocks
    private CarpoolingServiceImpl carpoolingService;

    @Mock
    private MatchDao matchDAO;

    @Mock
    private CarDao carDAO;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() throws Exception {
        ReflectionTestUtils.setField(carpoolingService, "carpoolingRequestsQueue",
                "http://localhost/test/requests");
        ReflectionTestUtils.setField(carpoolingService, "carpoolingUpdatesQueue",
                "http://localhost/test/updates");
    }

    @Test
    void confirmCarForSelfRegistration() throws Exception {
        // Given
        final Match match = new Match(1);
        final Player player = new Player(1);
        player.setEmail("test@email.com");
        final Car car = new Car(1);
        car.setDriver(player);
        final UserContext context = new UserContext("test@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(car);
        carpoolingService.updateCarpoolingInformation(match, player, car, true, context);

        // Then
        verify(carDAO).findCarById(anyInt());
        verify(matchDAO).updateCarForRegistration(any(Match.class), any(Player.class), any(Car.class), eq(true));
    }

    @Test
    void confirmCarForAnotherPlayerRegistration() throws Exception {
        // Given
        final Match match = new Match(1);
        match.setDate(LocalDateTime.now());
        final Player player = new Player(1);
        player.setEmail("test@email.com");
        final Car car = new Car(1);
        car.setDriver(player);
        final UserContext context = new UserContext("test@email.com");
        final Player anotherPlayer = new Player(2);
        anotherPlayer.setEmail("anotherplayer@email.com");
        final Registration registration = new Registration();
        registration.setCar(car);
        registration.setPlayer(anotherPlayer);
        registration.setCarConfirmed(false);

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(car);
        when(matchDAO.loadRegistration(any(Match.class), any(Player.class))).thenReturn(registration);
        carpoolingService.updateCarpoolingInformation(match, anotherPlayer, car, true, context);

        // Then
        verify(carDAO).findCarById(anyInt());
        verify(matchDAO).loadRegistration(any(Match.class), any(Player.class));
        verify(matchDAO).updateCarForRegistration(any(Match.class), any(Player.class), any(Car.class), eq(true));
        verify(rabbitTemplate).convertAndSend(anyString(), any(CarpoolingUpdateEvent.class));
    }

    @Test
    void confirmCarForRegistrationUnauthorisedUser() {
        // Given
        final Match match = new Match(1);
        final Player player = new Player(1);
        player.setEmail("test@email.com");
        final Car car = new Car(1);
        car.setDriver(player);
        final UserContext context = new UserContext("anotheruser@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(car);
        // Then
        assertThrows(UnauthorisedUserException.class,
                () -> carpoolingService.updateCarpoolingInformation(match, player, car, true, context));
    }

    @Test
    void disproveCarForRegistration() throws Exception {
        // Given
        final Match match = new Match(1);
        final Player player = new Player(1);
        player.setEmail("test@email.com");
        final Car car = new Car(1);
        car.setDriver(player);
        final Registration registration = new Registration();
        registration.setId(new RegistrationId(1, 1));
        registration.setPlayer(player);
        registration.setCar(car);
        final UserContext context = new UserContext("test@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(car);
        carpoolingService.updateCarpoolingInformation(match, player, car, false, context);

        // Then
        verify(matchDAO).updateCarForRegistration(any(Match.class), any(Player.class), any(Car.class), eq(false));
    }

    @Test
    void disproveCarForRegistrationUnauthorisedUser() {
        // Given
        final Match match = new Match(1);
        final Player player = new Player(1);
        player.setEmail("test@email.com");
        final Car car = new Car(1);
        car.setDriver(player);
        final Registration registration = new Registration();
        registration.setId(new RegistrationId(1, 1));
        registration.setPlayer(player);
        registration.setCar(car);
        final UserContext context = new UserContext("anotheruser@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(car);

        // Then
        assertThrows(UnauthorisedUserException.class,
                () -> carpoolingService.updateCarpoolingInformation(match, player,  car, false, context));
    }

    @Test
    void processCarSeatRequest() throws Exception {
        // Given
        final Match match = new Match(1);
        match.setDate(LocalDateTime.now());
        final Player player = new Player(1);
        player.setFirstName("First-Name");
        final Player driver = new Player(2);
        driver.setFirstName("Driver");
        driver.setEmail("driver@email.com");
        final Car car = new Car(1);
        car.setDriver(driver);

        // When
        assertDoesNotThrow(() -> carpoolingService.processCarSeatRequest(match, player, car, new UserContext()));

        // Then
        verify(rabbitTemplate).convertAndSend(anyString(), any(CarpoolingRequestEvent.class));
    }

    @Test
    void processTransportationChangeCarpoolingEnabledDriver2cars() throws Exception {
        // Given
        final String username = "test@email.com";
        final Match match = new Match(1);
        match.setCarpoolingEnabled(true);
        final Player driver = new Player(1, "test", "test", username, "test");
        final Car car1 = new Car(1);
        car1.setNumSeats(3);
        car1.setDriver(driver);
        final Car car2 = new Car(2);
        car2.setNumSeats(3);
        car2.setDriver(driver);
        final UserContext ctx = new UserContext(username);

        final Player player2 = new Player(2);
        Registration registrationPlayer2 = new Registration();
        registrationPlayer2.setId(new RegistrationId(match.getId(), player2.getId()));
        registrationPlayer2.setPlayer(player2);
        registrationPlayer2.setCar(car1);
        registrationPlayer2.setCarConfirmed(true);

        final Player player3 = new Player(3);
        Registration registrationPlayer3 = new Registration();
        registrationPlayer3.setId(new RegistrationId(match.getId(), player3.getId()));
        registrationPlayer3.setPlayer(player3);
        registrationPlayer3.setCar(car1);
        registrationPlayer3.setCarConfirmed(true);

        final List<Registration> registrations = List.of(registrationPlayer2, registrationPlayer3);

        // When
        when(matchDAO.findPassengerRegistrationsByCar(any(Match.class), any(Car.class))).thenReturn(registrations);
        when(carDAO.findCarById(anyInt())).thenReturn(car2);

        carpoolingService.processTransportationChange(match, car1, car2, ctx);

        // Then
        verify(matchDAO, times(2)).updateCarForRegistration(any(Match.class), any(Player.class),
                any(Car.class), anyBoolean());
        verify(matchDAO, never()).resetCarDetails(any(Match.class), any(Player.class));
    }

    @Test
    void processTransportationChangeCarpoolingEnabledDriverSmallerCar() throws Exception {
        // Given
        final String username = "test@email.com";
        final Match match = new Match(1);
        match.setCarpoolingEnabled(true);
        final Player driver = new Player(1, "test", "test", username, "test");
        final Car car1 = new Car(1);
        car1.setNumSeats(3);
        car1.setDriver(driver);
        final Car car2 = new Car(2);
        car2.setNumSeats(1);
        car2.setDriver(driver);
        final UserContext ctx = new UserContext(username);

        final Player player2 = new Player(2);
        Registration registrationPlayer2 = new Registration();
        registrationPlayer2.setId(new RegistrationId(match.getId(), player2.getId()));
        registrationPlayer2.setPlayer(player2);
        registrationPlayer2.setCar(car1);
        registrationPlayer2.setCarConfirmed(true);

        final Player player3 = new Player(3);
        Registration registrationPlayer3 = new Registration();
        registrationPlayer3.setId(new RegistrationId(match.getId(), player3.getId()));
        registrationPlayer3.setPlayer(player3);
        registrationPlayer3.setCar(car1);
        registrationPlayer3.setCarConfirmed(true);

        final List<Registration> registrations = List.of(registrationPlayer2, registrationPlayer3);

        // When
        when(matchDAO.findPassengerRegistrationsByCar(any(Match.class), any(Car.class))).thenReturn(registrations);
        when(carDAO.findCarById(anyInt())).thenReturn(car2);

        try {
            carpoolingService.processTransportationChange(match, car1, car2, ctx);
            fail("This case should throw an exception as the new car has no seats for everyone");
        } catch (ApplicationException e) {
            assertNotNull(e.getCode());
            assertNotNull(e.getMessage());
            assertEquals("carpooling.passengers.transfer.failed", e.getCode());
        }

        // Then
        verify(matchDAO, never()).updateCarForRegistration(any(Match.class), any(Player.class), any(Car.class),
                anyBoolean());
        verify(matchDAO, never()).resetCarDetails(any(Match.class), any(Player.class));
    }


    @Test
    void processTransportationChangeCarpoolingEnabledDriverUnregisteredCar() throws Exception {
        // Given
        final String username = "test@email.com";
        final Match match = new Match(1);
        match.setCarpoolingEnabled(true);
        final Player driver = new Player(1, "test", "test", username, "test");
        final Car car1 = new Car(1);
        car1.setNumSeats(3);
        car1.setDriver(driver);
        final Car car2 = new Car(2);
        car2.setNumSeats(3);
        car2.setDriver(driver);
        final UserContext ctx = new UserContext(username);

        final Player player2 = new Player(2);
        Registration registrationPlayer2 = new Registration();
        registrationPlayer2.setId(new RegistrationId(match.getId(), player2.getId()));
        registrationPlayer2.setPlayer(player2);
        registrationPlayer2.setCar(car1);
        registrationPlayer2.setCarConfirmed(true);

        final Player player3 = new Player(3);
        Registration registrationPlayer3 = new Registration();
        registrationPlayer3.setId(new RegistrationId(match.getId(), player3.getId()));
        registrationPlayer3.setPlayer(player3);
        registrationPlayer3.setCar(car1);
        registrationPlayer3.setCarConfirmed(true);

        final List<Registration> registrations = List.of(registrationPlayer2, registrationPlayer3);

        // When
        when(matchDAO.findPassengerRegistrationsByCar(any(Match.class), any(Car.class))).thenReturn(registrations);
        when(carDAO.findCarById(anyInt())).thenReturn(null);

        try {
            carpoolingService.processTransportationChange(match, car1, car2, ctx);
            fail("This case should throw an exception as the new car is not registered");
        } catch (ApplicationException e) {
            assertNotNull(e.getCode());
            assertNotNull(e.getMessage());
            assertEquals("carpooling.passengers.transfer.failed", e.getCode());
        }

        // Then
        verify(matchDAO, never()).updateCarForRegistration(any(Match.class), any(Player.class), any(Car.class),
                anyBoolean());
        verify(matchDAO, never()).resetCarDetails(any(Match.class), any(Player.class));
    }

    @Test
    void processTransportationChangeCarpoolingEnabledDriverOldCar() throws Exception {
        // Given
        final String username = "test@email.com";
        final Match match = new Match(1);
        match.setCarpoolingEnabled(true);
        final Player driver1 = new Player(1, "test", "test", username, "test");
        final Player driver10 = new Player(10, "test", "test", "another@email.com", "test");
        final Car car1 = new Car(1);
        car1.setNumSeats(3);
        car1.setDriver(driver1);
        final Car car2 = new Car(2);
        car2.setNumSeats(3);
        car2.setDriver(driver10);
        final UserContext ctx = new UserContext(username);

        final Player player2 = new Player(2);
        final Registration registrationPlayer2 = new Registration();
        registrationPlayer2.setId(new RegistrationId(match.getId(), player2.getId()));
        registrationPlayer2.setPlayer(player2);
        registrationPlayer2.setCar(car1);
        registrationPlayer2.setCarConfirmed(true);

        final Player player3 = new Player(3);
        final Registration registrationPlayer3 = new Registration();
        registrationPlayer3.setId(new RegistrationId(match.getId(), player3.getId()));
        registrationPlayer3.setPlayer(player3);
        registrationPlayer3.setCar(car1);
        registrationPlayer3.setCarConfirmed(true);

        final List<Registration> registrations = List.of(registrationPlayer2, registrationPlayer3);

        // When
        when(matchDAO.findPassengerRegistrationsByCar(any(Match.class), any(Car.class))).thenReturn(registrations);
        when(carDAO.findCarById(anyInt())).thenReturn(car2);

        carpoolingService.processTransportationChange(match, car1, car2, ctx);

        // Then
        verify(matchDAO, never()).updateCarForRegistration(any(Match.class), any(Player.class), any(Car.class),
                anyBoolean());
        verify(matchDAO, times(2)).resetCarDetails(any(Match.class), any(Player.class));
    }

    @Test
    void findAvailableCarsByMatchWithCarpoolingEnabled() throws Exception {
        // Given
        Match match = new Match(1);
        match.setCarpoolingEnabled(true);

        Car car1 = new Car(1);
        Car car2 = new Car(2);

        // When
        when(carDAO.findCarsByMatch(any(Match.class))).thenReturn(List.of(car1, car2));
        List<Car> cars = carpoolingService.findAvailableCarsByMatch(match);

        // Then
        assertNotNull(cars);
        assertEquals(2, cars.size());
        verify(carDAO).findCarsByMatch(any(Match.class));
    }

    @Test
    void findAvailableCarsByMatchWithCarpoolingDisabled() throws Exception {
        // Given
        Match match = new Match(1);
        match.setCarpoolingEnabled(false);

        // When
        List<Car> cars = carpoolingService.findAvailableCarsByMatch(match);

        // Then
        assertNotNull(cars);
        assertEquals(0, cars.size());
        verify(carDAO, never()).findCarsByMatch(any(Match.class));
    }
}
