package be.ucll.carservice.domain;

import be.ucll.carservice.api.model.CarListingCommand;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
        return repository.findById(carId).orElse(null);
    }

    public Car getCarById(Integer carId) {
        return repository.findById(carId).orElse(null);
    }

    public Car updateAvailability(Integer carId, Boolean available, String ownerEmail) {
        Car car = repository.findById(carId).orElseThrow();
        if(!car.getOwnerEmail().equals(ownerEmail)) {
            return null;
        }
        car.setAvailable(available);
        return repository.save(car);
    }

    public List<Car> searchCar(String location, String carModel, BigDecimal price) {
        return repository.findAllByInfo(location, carModel, price);
    }

    public void deleteCarById(Integer carId, String ownerEmail) {
        Car car = repository.findById(carId).orElseThrow();
        if(car.getOwnerEmail().equals(ownerEmail)) {
            repository.delete(car);
        } else {
            throw new IllegalArgumentException("Owner email does not match");
        }
    }
}
