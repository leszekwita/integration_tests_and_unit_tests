package pl.radzik.ksb2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.radzik.ksb2.domain.Car;
import pl.radzik.ksb2.repository.CarRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {


    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        carRepository.findAll().iterator().forEachRemaining(cars::add);
        return cars;

    }

    public Optional<Car> getCarById(String id) {
        return carRepository.findById(Long.valueOf(id));
    }

    public List<Car> getCarByColor(String color) {
        List<Car> cars = getAllCars();
        return cars.stream().filter(car -> color.equals(car.getColor())).collect(Collectors.toList());
    }

    public boolean addCar(Car car) {
        Car added = carRepository.save(car);
        if (added != null) {
            return true;
        }
        return false;
    }

    public boolean removeCar(Car car) {
        boolean removed = false;
        Optional<Car> carFound = getCarById(String.valueOf(car.getId()));
        if (carFound.isPresent()) {
            if (carFound != null) {
                try {
                    carRepository.delete(carFound.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                removed = true;
            }
        }
        return removed;
    }

    public boolean modifyCar(Car car) {
        Car added = carRepository.save(car);
        if (added != null) {
            return true;
        }
        return false;
    }


    public List<Car> fillInCars(String colorFound, String colorSet) {

        List<Car> cars = carRepository.findAllByColor(colorFound);
        cars.stream().forEach(car -> car.setColor(colorSet));
        return cars;
    }
}
