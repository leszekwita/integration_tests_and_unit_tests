package pl.radzik.ksb2.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.radzik.ksb2.domain.Car;
import pl.radzik.ksb2.service.CarService;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/cars")
public class CarController {

private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CollectionModel<Car>> getAllCars() {
        Link link = linkTo(CarController.class).withSelfRel();
        CollectionModel<Car> carResources = CollectionModel.of(carService.getAllCars(), link);
        return new ResponseEntity<CollectionModel<Car>>(carResources,HttpStatus.OK);
    }

    @GetMapping(value = "/{id}",produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<Car>> getCarById (@PathVariable String id) {
        Link link = linkTo(CarController.class).slash(id).withSelfRel();
        Optional<Car> carfounded = carService.getCarById(id);
        EntityModel<Car> carResources = EntityModel.of(carfounded.get(), link);
        if(carfounded.isPresent()) {
            return new ResponseEntity<EntityModel<Car>>(carResources, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/color/{color}" ,produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<Car>> getCarByColor (@PathVariable String color) {
        Optional<Car> carfounded = carService.getCarByColor(color);
        Link link = linkTo(CarController.class).slash(color).withSelfRel();
        EntityModel<Car> carResources = EntityModel.of(carfounded.get(), link);
        if(carfounded.isPresent()) {
            return new ResponseEntity<EntityModel<Car>>(carResources, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Car> addCar(@RequestBody Car car){
        boolean added = carService.addCar(car);
       if(added){
           return new ResponseEntity<>(HttpStatus.CREATED);
       }
       return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Car> modifyCar(@RequestBody Car newCar){

        Optional<Car> carfounded = carService.getCarById(String.valueOf(newCar.getId()));
        if(carfounded.isPresent()) {
            carService.removeCar(carfounded.get());
            carService.addCar(newCar);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Car> removeCar(@PathVariable("id") Long id){
        Optional<Car> carfounded = carService.getCarById(String.valueOf(id));
        if(carfounded.isPresent()) {
            carService.removeCar(carfounded.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value ="/{id}/{attribute}/{value}" , produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Car> partialModifyCar(@PathVariable long id, @PathVariable String attribute, @PathVariable String value) {

        Optional<Car> carfounded = carService.getCarById(String.valueOf(id));
        if(carfounded.isPresent()) {
          if(attribute.equals("mark")) {
              carfounded.get().setMark(value);
          }
         if (attribute.equals("color")){
             carfounded.get().setColor(value);
         }
         if(attribute.equals("model")){
             carfounded.get().setModel(value);
         }
        }
         else{
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
