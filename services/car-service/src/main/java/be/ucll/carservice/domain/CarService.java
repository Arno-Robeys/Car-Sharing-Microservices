package be.ucll.carservice.domain;

import org.springframework.stereotype.Component;

@Component
public class CarService {
    private final CarRepository repository;

    public CarService(CarRepository repository) {
        this.repository = repository;
    }
}
