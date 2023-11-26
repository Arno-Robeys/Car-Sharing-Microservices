package be.ucll.carservice.domain;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CarService {
    private final CarRepository repository;

    public CarService(CarRepository repository) {
        this.repository = repository;
    }
}
