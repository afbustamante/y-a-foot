package net.andresbustamante.yafoot.dao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import net.andresbustamante.yafoot.model.Car;
import net.andresbustamante.yafoot.model.Player;
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
        int nbLignesInserees = carDAO.saveCar(newCar);

        // Then
        assertEquals(1, nbLignesInserees);
        assertNotNull(newCar.getId());
        assertTrue(newCar.getId() > 0);
    }

    @Test
    void deleteCarsForPlayer() throws Exception {
        // When
        int nbVoituresSupprimees = carDAO.deleteCarsByPlayer(player);

        // Then
        assertEquals(2, nbVoituresSupprimees);
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
}