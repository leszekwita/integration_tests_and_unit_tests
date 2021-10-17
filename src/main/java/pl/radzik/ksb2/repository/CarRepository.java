package pl.radzik.ksb2.repository;

import org.springframework.data.repository.CrudRepository;
import pl.radzik.ksb2.domain.Car;

import java.util.List;

public interface CarRepository extends CrudRepository<Car, Long> {

    List<Car> findAllByColor(String color);
}
