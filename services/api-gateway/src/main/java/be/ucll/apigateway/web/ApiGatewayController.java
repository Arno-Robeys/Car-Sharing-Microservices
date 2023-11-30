package be.ucll.apigateway.web;

import be.ucll.apigateway.api.model.*;
import be.ucll.apigateway.client.car.api.CarListingApi;
import be.ucll.apigateway.client.reservation.api.ReservationApi;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import com.netflix.discovery.EurekaClient;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import be.ucll.apigateway.api.ApiGatewayApiDelegate;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.List;


@Component
public class ApiGatewayController implements ApiGatewayApiDelegate {

    @Autowired
    private EurekaClient discoveryClient;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    private CarListingApi carListingApi;

    @Autowired
    private ReservationApi reservationApi;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public ResponseEntity<ApiReservationResponse> createReservation(ReservationCommand reservationCommand) {
        reservationApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("reservation-service", false).getHomePageUrl());

        be.ucll.apigateway.client.reservation.model.ReservationCommand reservationCommandRequest = modelMapper.map(reservationCommand, be.ucll.apigateway.client.reservation.model.ReservationCommand.class);

        ApiReservationResponse responseEntity;
        try {
            responseEntity = modelMapper.map(circuitBreakerFactory.create("reservationapi").run(() -> reservationApi.createReservation(reservationCommandRequest)), ApiReservationResponse.class);
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiCar> newCarListing(CarListingCommand carListingCommand) {
        carListingApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("car-service", false).getHomePageUrl());

        be.ucll.apigateway.client.car.model.CarListingCommand carListingCommandRequest = modelMapper.map(carListingCommand, be.ucll.apigateway.client.car.model.CarListingCommand.class);

        ApiCar responseEntity;
        try {
            responseEntity = modelMapper.map(circuitBreakerFactory.create("carlistingapi").run(() -> carListingApi.newCarListing(carListingCommandRequest)), ApiCar.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ApiCar>> searchCar(String location, String carModel, BigDecimal price) {
        carListingApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("car-service", false).getHomePageUrl());

        List<ApiCar> responseEntity;
        try {
            responseEntity = modelMapper.map(circuitBreakerFactory.create("carlistingapi").run(() -> carListingApi.searchCar(location, carModel, price)), new TypeToken<List<ApiCar>>(){}.getType());
        } catch (RestClientException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiCar> updateCarById(Integer carId, UpdateAvailabilityCar updateAvailabilityCar) {
        carListingApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("car-service", false).getHomePageUrl());

        be.ucll.apigateway.client.car.model.UpdateAvailabilityCar updateCar = modelMapper.map(updateAvailabilityCar, be.ucll.apigateway.client.car.model.UpdateAvailabilityCar.class);

        ApiCar responseEntity;
        try {
            responseEntity = modelMapper.map(circuitBreakerFactory.create("carlistingapi").run(() -> carListingApi.updateCarById(carId, updateCar)), ApiCar.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> confirmReservation(ConfirmingReservationCommand confirmingReservationCommand) {
        reservationApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("reservation-service", false).getHomePageUrl());

        be.ucll.apigateway.client.reservation.model.ConfirmingReservationCommand confirmingReservationCommandRequest = modelMapper.map(confirmingReservationCommand, be.ucll.apigateway.client.reservation.model.ConfirmingReservationCommand.class);

        try {
            circuitBreakerFactory.create("reservationapi").run(() -> reservationApi.confirmReservation(confirmingReservationCommandRequest));
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteCarById(Integer carId, String ownerEmail) {
        carListingApi.getApiClient().setBasePath(discoveryClient.getNextServerFromEureka("car-service", false).getHomePageUrl());

        try {
            circuitBreakerFactory.create("carlistingapi").run(() -> {carListingApi.deleteCarById(carId, ownerEmail); return null;});
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            if(e.getMessage().contains("400")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
