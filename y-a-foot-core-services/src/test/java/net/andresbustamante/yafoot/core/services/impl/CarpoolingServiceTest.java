package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.commons.services.AbstractServiceTest;
import net.andresbustamante.yafoot.core.dao.CarDAO;
import net.andresbustamante.yafoot.core.dao.MatchDAO;
import net.andresbustamante.yafoot.core.exceptions.UnauthorisedUserException;
import net.andresbustamante.yafoot.core.model.*;
import net.andresbustamante.yafoot.messaging.services.MessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarpoolingServiceTest extends AbstractServiceTest {

    @InjectMocks
    private CarpoolingServiceImpl carpoolingService;

    @Mock
    private MatchDAO matchDAO;

    @Mock
    private CarDAO carDAO;

    @Mock
    private MessagingService messagingService;

    @BeforeEach
    void setUp() throws Exception {
        ReflectionTestUtils.setField(carpoolingService, "carpoolingManagementUrl",
                "http://localhost/api/test/{0}");
        ReflectionTestUtils.setField(carpoolingService, "matchManagementUrl",
                "http://localhost/api/test/{0}");
    }

    @Test
    void confirmCarForSelfRegistration() throws Exception {
        // Given
        Match match = new Match(1);
        Player player = new Player(1);
        player.setEmail("test@email.com");
        Car car = new Car(1);
        car.setDriver(player);
        UserContext context = new UserContext("test@email.com");

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
        Match match = new Match(1);
        match.setDate(OffsetDateTime.now());
        Player player = new Player(1);
        player.setEmail("test@email.com");
        Car car = new Car(1);
        car.setDriver(player);
        UserContext context = new UserContext("test@email.com");
        Player anotherPlayer = new Player(2);
        anotherPlayer.setEmail("anotherplayer@email.com");
        Registration registration = new Registration();
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
        verify(messagingService).sendEmail(anyString(), anyString(), eq(null), anyString(), any(CarpoolingRequest.class), any(Locale.class));
    }

    @Test
    void confirmCarForRegistrationUnauthorisedUser() {
        // Given
        Match match = new Match(1);
        Player player = new Player(1);
        player.setEmail("test@email.com");
        Car car = new Car(1);
        car.setDriver(player);
        UserContext context = new UserContext("anotheruser@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(car);
        // Then
        assertThrows(UnauthorisedUserException.class,
                () -> carpoolingService.updateCarpoolingInformation(match, player, car, true, context));
    }

    @Test
    void disproveCarForRegistration() throws Exception {
        // Given
        Match match = new Match(1);
        Player player = new Player(1);
        player.setEmail("test@email.com");
        Car car = new Car(1);
        car.setDriver(player);
        Registration registration = new Registration();
        registration.setId(new RegistrationId(1, 1));
        registration.setPlayer(player);
        registration.setCar(car);
        UserContext context = new UserContext("test@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(car);
        carpoolingService.updateCarpoolingInformation(match, player, car, false, context);

        // Then
        verify(matchDAO).updateCarForRegistration(any(Match.class), any(Player.class), any(Car.class), eq(false));
    }

    @Test
    void disproveCarForRegistrationUnauthorisedUser() {
        // Given
        Match match = new Match(1);
        Player player = new Player(1);
        player.setEmail("test@email.com");
        Car car = new Car(1);
        car.setDriver(player);
        Registration registration = new Registration();
        registration.setId(new RegistrationId(1, 1));
        registration.setPlayer(player);
        registration.setCar(car);
        UserContext context = new UserContext("anotheruser@email.com");

        // When
        when(carDAO.findCarById(anyInt())).thenReturn(car);

        // Then
        assertThrows(UnauthorisedUserException.class,
                () -> carpoolingService.updateCarpoolingInformation(match, player,  car,false, context));
    }

    @Test
    void processCarSeatRequest() throws Exception {
        // Given
        Match match = new Match(1);
        match.setDate(OffsetDateTime.now());
        Player player = new Player(1);
        player.setFirstName("First-Name");
        Player driver = new Player(2);
        driver.setFirstName("Driver");
        driver.setEmail("driver@email.com");
        Car car = new Car(1);
        car.setDriver(driver);

        // When
        carpoolingService.processCarSeatRequest(match, player, car, new UserContext());

        // Then
        verify(messagingService).sendEmail(anyString(), anyString(), any(), anyString(), any(CarpoolingRequest.class), any(Locale.class));
    }

    @Test
    void processTransportationChangeCarpoolingEnabledDriver2cars() throws Exception {
        // Given
        String username = "test@email.com";
        Match match = new Match(1);
        match.setCarpoolingEnabled(true);
        Player driver = new Player(1, "test", "test", username, "test");
        Car car1 = new Car(1);
        car1.setNumSeats(3);
        car1.setDriver(driver);
        Car car2 = new Car(2);
        car2.setNumSeats(3);
        car2.setDriver(driver);
        UserContext ctx = new UserContext(username);

        Player player2 = new Player(2);
        Registration registrationPlayer2 = new Registration();
        registrationPlayer2.setId(new RegistrationId(match.getId(), player2.getId()));
        registrationPlayer2.setPlayer(player2);
        registrationPlayer2.setCar(car1);
        registrationPlayer2.setCarConfirmed(true);

        Player player3 = new Player(3);
        Registration registrationPlayer3 = new Registration();
        registrationPlayer3.setId(new RegistrationId(match.getId(), player3.getId()));
        registrationPlayer3.setPlayer(player3);
        registrationPlayer3.setCar(car1);
        registrationPlayer3.setCarConfirmed(true);

        List<Registration> registrations = List.of(registrationPlayer2, registrationPlayer3);

        // When
        when(matchDAO.findPassengerRegistrationsByCar(any(Match.class), any(Car.class))).thenReturn(registrations);
        when(carDAO.findCarById(anyInt())).thenReturn(car2);

        carpoolingService.processTransportationChange(match, car1, car2, ctx);

        // Then
        verify(matchDAO, times(2)).updateCarForRegistration(any(Match.class), any(Player.class), any(Car.class), anyBoolean());
        verify(matchDAO, never()).resetCarDetails(any(Match.class), any(Player.class));
    }

    @Test
    void processTransportationChangeCarpoolingEnabledDriverSmallerCar() throws Exception {
        // Given
        String username = "test@email.com";
        Match match = new Match(1);
        match.setCarpoolingEnabled(true);
        Player driver = new Player(1, "test", "test", username, "test");
        Car car1 = new Car(1);
        car1.setNumSeats(3);
        car1.setDriver(driver);
        Car car2 = new Car(2);
        car2.setNumSeats(1);
        car2.setDriver(driver);
        UserContext ctx = new UserContext(username);

        Player player2 = new Player(2);
        Registration registrationPlayer2 = new Registration();
        registrationPlayer2.setId(new RegistrationId(match.getId(), player2.getId()));
        registrationPlayer2.setPlayer(player2);
        registrationPlayer2.setCar(car1);
        registrationPlayer2.setCarConfirmed(true);

        Player player3 = new Player(3);
        Registration registrationPlayer3 = new Registration();
        registrationPlayer3.setId(new RegistrationId(match.getId(), player3.getId()));
        registrationPlayer3.setPlayer(player3);
        registrationPlayer3.setCar(car1);
        registrationPlayer3.setCarConfirmed(true);

        List<Registration> registrations = List.of(registrationPlayer2, registrationPlayer3);

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
        verify(matchDAO, never()).updateCarForRegistration(any(Match.class), any(Player.class), any(Car.class), anyBoolean());
        verify(matchDAO, never()).resetCarDetails(any(Match.class), any(Player.class));
    }


    @Test
    void processTransportationChangeCarpoolingEnabledDriverUnregisteredCar() throws Exception {
        // Given
        String username = "test@email.com";
        Match match = new Match(1);
        match.setCarpoolingEnabled(true);
        Player driver = new Player(1, "test", "test", username, "test");
        Car car1 = new Car(1);
        car1.setNumSeats(3);
        car1.setDriver(driver);
        Car car2 = new Car(2);
        car2.setNumSeats(3);
        car2.setDriver(driver);
        UserContext ctx = new UserContext(username);

        Player player2 = new Player(2);
        Registration registrationPlayer2 = new Registration();
        registrationPlayer2.setId(new RegistrationId(match.getId(), player2.getId()));
        registrationPlayer2.setPlayer(player2);
        registrationPlayer2.setCar(car1);
        registrationPlayer2.setCarConfirmed(true);

        Player player3 = new Player(3);
        Registration registrationPlayer3 = new Registration();
        registrationPlayer3.setId(new RegistrationId(match.getId(), player3.getId()));
        registrationPlayer3.setPlayer(player3);
        registrationPlayer3.setCar(car1);
        registrationPlayer3.setCarConfirmed(true);

        List<Registration> registrations = List.of(registrationPlayer2, registrationPlayer3);

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
        verify(matchDAO, never()).updateCarForRegistration(any(Match.class), any(Player.class), any(Car.class), anyBoolean());
        verify(matchDAO, never()).resetCarDetails(any(Match.class), any(Player.class));
    }

    @Test
    void processTransportationChangeCarpoolingEnabledDriverOldCar() throws Exception {
        // Given
        String username = "test@email.com";
        Match match = new Match(1);
        match.setCarpoolingEnabled(true);
        Player driver1 = new Player(1, "test", "test", username, "test");
        Player driver10 = new Player(10, "test", "test", "another@email.com", "test");
        Car car1 = new Car(1);
        car1.setNumSeats(3);
        car1.setDriver(driver1);
        Car car2 = new Car(2);
        car2.setNumSeats(3);
        car2.setDriver(driver10);
        UserContext ctx = new UserContext(username);

        Player player2 = new Player(2);
        Registration registrationPlayer2 = new Registration();
        registrationPlayer2.setId(new RegistrationId(match.getId(), player2.getId()));
        registrationPlayer2.setPlayer(player2);
        registrationPlayer2.setCar(car1);
        registrationPlayer2.setCarConfirmed(true);

        Player player3 = new Player(3);
        Registration registrationPlayer3 = new Registration();
        registrationPlayer3.setId(new RegistrationId(match.getId(), player3.getId()));
        registrationPlayer3.setPlayer(player3);
        registrationPlayer3.setCar(car1);
        registrationPlayer3.setCarConfirmed(true);

        List<Registration> registrations = List.of(registrationPlayer2, registrationPlayer3);

        // When
        when(matchDAO.findPassengerRegistrationsByCar(any(Match.class), any(Car.class))).thenReturn(registrations);
        when(carDAO.findCarById(anyInt())).thenReturn(car2);

        carpoolingService.processTransportationChange(match, car1, car2, ctx);

        // Then
        verify(matchDAO, never()).updateCarForRegistration(any(Match.class), any(Player.class), any(Car.class), anyBoolean());
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