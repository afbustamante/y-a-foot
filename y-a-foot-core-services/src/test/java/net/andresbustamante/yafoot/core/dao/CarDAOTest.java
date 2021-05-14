package net.andresbustamante.yafoot.core.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.core.model.Car;
import net.andresbustamante.yafoot.core.model.Match;
import net.andresbustamante.yafoot.core.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static org.junit.jupiter.api.Assertions.*;

@DatabaseSetup(value = "classpath:datasets/carsDataset.xml")
@DatabaseTearDown(value = "classpath:datasets/carsDataset.xml", type = DELETE_ALL)
class CarDAOTest extends AbstractDAOTest {

    @Autowired
    private CarDAO carDAO;

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(1);
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
        assertNotNull(car1.getDriver().getEmail());
        assertEquals(player.getId(), car1.getDriver().getId());
        assertEquals(player.getEmail(), car1.getDriver().getEmail());
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
        // Given
        Player player = new Player(1);

        List<Car> cars = carDAO.findCarsByPlayer(player);

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
        Match match = new Match(1);
        Car car1 = new Car(1001);
        Car car3 = new Car(1003);

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
}