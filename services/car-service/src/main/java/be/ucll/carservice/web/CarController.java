package be.ucll.carservice.web;

import be.ucll.carservice.api.CarListingApiDelegate;
import be.ucll.carservice.api.model.ApiCar;
import be.ucll.carservice.api.model.CarListingCommand;
import be.ucll.carservice.domain.Car;
import be.ucll.carservice.domain.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CarController implements CarListingApiDelegate {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Override
    public ResponseEntity<ApiCar> newCarListing(CarListingCommand carListingCommand)  {
        ApiCar apiCar = new ApiCar();
        Car car = carService.newCarListing(carListingCommand);

        apiCar.setCarId(car.getId());
        apiCar.setOwnerEmail(car.getOwnerEmail());
        apiCar.setCarModel(car.getCarModel());
        apiCar.setYear(car.getYear());
        apiCar.setLocation(car.getLocation());
        apiCar.setPrice(car.getPrice());
        apiCar.setAvailable(car.getAvailable());

        return ResponseEntity.ok(apiCar);
    }
}
