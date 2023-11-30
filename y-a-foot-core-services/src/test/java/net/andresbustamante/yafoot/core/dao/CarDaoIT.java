package net.andresbustamante.yafoot.core.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.commons.config.DbUnitTestConfig;
import net.andresbustamante.yafoot.commons.config.JdbcTestConfig;
import net.andresbustamante.yafoot.commons.dao.AbstractDaoIntegrationTest;
import net.andresbustamante.yafoot.core.config.MyBatisTestConfig;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {JdbcTestConfig.class, MyBatisTestConfig.class, DbUnitTestConfig.class})
@DatabaseSetup(value = "classpath:datasets/cars/t_player_match.csv")
@DatabaseTearDown(value = "classpath:datasets/cars/t_player_match.csv", type = DELETE_ALL)
class CarDaoIT extends AbstractDaoIntegrationTest {

    @Autowired
    private CarDao carDAO;

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(101);
        player.setEmail("john.doe@email.com");
    }

    @Test
    void findCarById() throws Exception {
        // When
        Car car1 = carDAO.findCarById(1001);

        // Then
        assertNotNull(car1);
        assertEquals("Peugeot 207", car1.getName());
        assertNotNull(car1.getNumSeats());
        assertEquals(4, car1.getNumSeats().intValue());
        assertNotNull(car1.getDriver());
        assertNotNull(car1.getDriver().getId());
        assertEquals(player.getId(), car1.getDriver().getId());
    }

    @Test
    void saveCar() throws Exception {
        // Given
        Car newCar = new Car();
        newCar.setName("Citroën DS3");
        newCar.setNumSeats(3);
        newCar.setDriver(player);

        // When
        int numLines = carDAO.saveCar(newCar);

        // Then
        assertEquals(1, numLines);
        assertNotNull(newCar.getId());
        assertTrue(newCar.getId() > 0);
    }

    @Test
    void findCarsByPlayer() throws Exception {
        // When
        List<Car> cars = carDAO.findCarsByPlayer(player);

        // Then
        assertNotNull(cars);
        assertEquals(2, cars.size());

        for (Car car : cars) {
            assertNotNull(car.getId());
            assertTrue(car.getId() > 0);
            assertNotNull(car.getName());
            assertNotNull(car.getNumSeats());
        }
    }

    @Test
    void findCarsByMatch() throws Exception {
        // Given
        final Match match = new Match(101);
        final Car car1 = new Car(1001);
        final Car car3 = new Car(1003);

        // When
        List<Car> cars = carDAO.findCarsByMatch(match);

        // Then
        assertNotNull(cars);
        assertEquals(2, cars.size());
        assertTrue(cars.contains(car1));
        assertTrue(cars.contains(car3));

        cars.forEach(car -> {
            assertNotNull(car.getDriver());
            assertNotNull(car.getDriver().getFirstName());
        });
    }

    @Test
    void testCarUsedForFutureMatch() throws Exception {
        // Given
        Car car = new Car(1001);

        // When
        var result = carDAO.isCarUsedForComingMatches(car);

        // Then
        assertTrue(result);
    }

    @Test
    void testCarUnusedForFutureMatch() throws Exception {
        // Given
        Car car = new Car(1002);

        // When
        var result = carDAO.isCarUsedForComingMatches(car);

        // Then
        assertFalse(result);
    }

    @Test
    void testUpdateCar() {
        // Given
        int carId = 1002;
        Car car = new Car(carId);
        car.setName("my new car");
        car.setNumSeats(1);

        // When
        var result = carDAO.updateCar(car);
        var storedCar = carDAO.findCarById(carId);

        // Then
        assertEquals(1, result);
        assertEquals("my new car", storedCar.getName());
        assertEquals(1, storedCar.getNumSeats());
        assertTrue(storedCar.getCreationDate().isBefore(storedCar.getModificationDate()));
    }

    @Test
    void testDeactivateCar() {
        // Given
        int carId = 1002;
        Car car = new Car(carId);

        // When
        var result = carDAO.deactivateCar(car);
        var storedCar = carDAO.findCarById(carId);

        // Then
        assertEquals(1, result);
        assertFalse(storedCar.isActive());
    }
}
