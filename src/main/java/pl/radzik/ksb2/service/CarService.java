package pl.radzik.ksb2.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.radzik.ksb2.domain.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    List<Car> cars = new ArrayList<>();

    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        cars.add(new Car(1, "Fiat", "Siena", "Blue"));
        cars.add(new Car(2, "Skoda", "Favorit", "Green"));
        cars.add(new Car(3, "Opel", "Meriva", "Black"));
        cars.add(new Car(4, "Kia", "Picanto", "Yellow"));
    }

    public List<Car> getAllCars() {
        return cars;
    }

    public Optional<Car> getCarById(String id) {
        return cars.stream().findFirst().filter(car -> Integer.valueOf(id) == car.getId());
    }

    public Optional<Car> getCarByColor(String color) {
        return cars.stream().findFirst().filter(car -> color.equals(car.getColor()));
    }

    public boolean addCar(Car car) {
        return cars.add(car);
    }

    public boolean removeCar(Car car) {
        return cars.remove(car);
    }
}
