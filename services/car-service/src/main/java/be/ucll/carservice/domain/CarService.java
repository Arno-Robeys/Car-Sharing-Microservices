package be.ucll.carservice.domain;

import be.ucll.carservice.api.model.CarListingCommand;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CarService {

    private final CarRepository repository;

    @Autowired
    public CarService(CarRepository repository) {
        this.repository = repository;
    }

    public Car newCarListing(CarListingCommand carListingCommand) {
        Car car = new Car();
        car.setOwnerEmail(carListingCommand.getOwnerEmail());
        car.setCarModel(carListingCommand.getCarModel());
        car.setYear(carListingCommand.getYear());
        car.setLocation(carListingCommand.getLocation());
        car.setPrice(carListingCommand.getPrice());
        car.setAvailable(true);

        return repository.save(car);
    }


    public Car reserveCar(Integer carId) {
        return repository.findById(carId).orElseThrow();
    }

    public String getOwnerEmail(Integer carId) {
        return repository.findById(carId).orElseThrow().getOwnerEmail();
    }
}
