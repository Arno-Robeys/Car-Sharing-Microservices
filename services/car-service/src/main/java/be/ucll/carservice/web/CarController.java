package be.ucll.carservice.web;

import be.ucll.carservice.api.CarListingApiDelegate;
import be.ucll.carservice.api.model.ApiCar;
import be.ucll.carservice.api.model.CarListingCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CarController implements CarListingApiDelegate {

    @Override
    public ResponseEntity<ApiCar> getCarById(Integer carId) {
        return null;
    }

    /*@Override
    public ResponseEntity<ApiCar> newCarListing(CarListingCommand carListingCommand)  {
        return null;
    }*/
}
