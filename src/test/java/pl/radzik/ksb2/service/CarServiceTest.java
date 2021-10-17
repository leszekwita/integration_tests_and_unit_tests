package pl.radzik.ksb2.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import pl.radzik.ksb2.domain.Car;
import pl.radzik.ksb2.repository.CarRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    private CarService carService;

    @BeforeEach
    private void setUpRepository() {
        // given
        CarRepository carRepository = mock(CarRepository.class);
        Car car = new Car();
        car.setColor("grey");
        car.setModel("Picanto");
        car.setMark("Kia");
        car.setId(1L);
        Car car2 = new Car();
        car2.setColor("orange");
        car2.setModel("Fiesta");
        car2.setMark("Ford");
        car2.setId(2L);
        Iterable<Car> all = Arrays.asList(car, car2);
        doReturn(all).when(carRepository).findAllByColor(Mockito.anyString());

        carService = new CarService(carRepository);
    }

    @Test
    public void shouldReturnSelectedCarsWithFilledColor() {
        // when
        List<Car> actual = carService.fillInCars("grey", "red");
        // then
        Assertions.assertEquals("red", actual.get(0).getColor());
        Assertions.assertEquals("Picanto", actual.get(0).getModel());
        Assertions.assertEquals("Kia", actual.get(0).getMark());
        Assertions.assertEquals(1L, actual.get(0).getId());
    }
}
