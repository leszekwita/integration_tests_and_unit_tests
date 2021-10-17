package pl.radzik.ksb2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.radzik.ksb2.domain.Car;
import pl.radzik.ksb2.service.CarService;

import java.util.*;


@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Car> getCarById(@PathVariable String id) {
        Optional<Car> carfound = carService.getCarById(id);
        if (carfound.isPresent()) {
            return ResponseEntity.of(carfound);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/color/{color}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Car>> getCarByColor(@PathVariable String color) {
        List<Car> carsfound = carService.getCarByColor(color);

        if (carsfound != null) {
            return ResponseEntity.ok(carsfound);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        boolean added = carService.addCar(car);
        if (added) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Car> modifyCar(@RequestBody Car newCar) {

        Optional<Car> carfounded = carService.getCarById(String.valueOf(newCar.getId()));
        if (carfounded.isPresent()) {
            carService.modifyCar(newCar);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Car> removeCar(@PathVariable("id") Long id) {
        Optional<Car> carfounded = carService.getCarById(String.valueOf(id));
        if (carfounded.isPresent()) {
            carService.removeCar(carfounded.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "/{id}/{attribute}/{value}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Car> partialModifyCar(@PathVariable long id, @PathVariable String attribute, @PathVariable String value) {

        Optional<Car> carfounded = carService.getCarById(String.valueOf(id));
        if (carfounded.isPresent()) {
            if (attribute.equals("mark")) {
                carfounded.get().setMark(value);
            }
            if (attribute.equals("color")) {
                carfounded.get().setColor(value);
            }
            if (attribute.equals("model")) {
                carfounded.get().setModel(value);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        carService.modifyCar(carfounded.get());
        return ResponseEntity.ok(carfounded.get());
    }
}
