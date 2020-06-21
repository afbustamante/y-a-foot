package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.CarDAO;
import net.andresbustamante.yafoot.dao.MatchDAO;
import net.andresbustamante.yafoot.exceptions.AuthorisationException;
import net.andresbustamante.yafoot.model.*;
import net.andresbustamante.yafoot.services.MessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;

import java.time.OffsetDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CarpoolingServiceImplTest extends AbstractServiceTest {

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
        MockitoAnnotations.initMocks(this);

        FieldSetter.setField(carpoolingService, CarpoolingServiceImpl.class.getDeclaredField("carpoolingManagementUrl"),
                "http://localhost/api/test/{0}");
    }

    @Test
    void confirmCarForRegistration() throws Exception {
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
        assertThrows(AuthorisationException.class,
                () -> carpoolingService.updateCarpoolingInformation(match, player, car, true, context));
    }

    @Test
    void unconfirmCarForRegistration() throws Exception {
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
    void unconfirmCarForRegistrationUnauthorisedUser() {
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
        assertThrows(AuthorisationException.class,
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
}