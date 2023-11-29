package be.ucll.carservice.web;

import be.ucll.carservice.api.CarListingApiDelegate;
import be.ucll.carservice.api.model.ApiCar;
import be.ucll.carservice.api.model.CarListingCommand;
import be.ucll.carservice.api.model.UpdateAvailabilityCar;
import be.ucll.carservice.domain.Car;
import be.ucll.carservice.domain.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CarController implements CarListingApiDelegate {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Override
    public ResponseEntity<ApiCar> newCarListing(CarListingCommand carListingCommand)  {
        Car car = carService.newCarListing(carListingCommand);
        return ResponseEntity.ok(generateResponse(car));
    }

    @Override
    public ResponseEntity<List<ApiCar>> searchCar(String location, String carModel, BigDecimal price) {
        List<Car> cars = carService.searchCar(location, carModel, price);

        return ResponseEntity.ok().body(cars.stream().map(this::generateResponse).toList());
    }

    @Override
    public ResponseEntity<ApiCar> updateCarById(Integer carId, UpdateAvailabilityCar updateAvailabilityCar) {
        Car car = carService.updateAvailability(carId, updateAvailabilityCar.getAvailable(), updateAvailabilityCar.getOwnerEmail());

        if(car == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(generateResponse(car));
    }

    public ApiCar generateResponse(Car car) {
        ApiCar apiCar = new ApiCar();

        apiCar.setCarId(car.getId());
        apiCar.setOwnerEmail(car.getOwnerEmail());
        apiCar.setCarModel(car.getCarModel());
        apiCar.setYear(car.getYear());
        apiCar.setLocation(car.getLocation());
        apiCar.setPrice(car.getPrice());
        apiCar.setAvailable(car.getAvailable());
        return apiCar;
    }
}
