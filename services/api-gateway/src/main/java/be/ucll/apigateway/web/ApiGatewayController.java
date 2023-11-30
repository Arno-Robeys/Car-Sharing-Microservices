package be.ucll.apigateway.web;

import be.ucll.apigateway.api.model.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import be.ucll.apigateway.api.ApiGatewayApiDelegate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;


@Component
public class ApiGatewayController implements ApiGatewayApiDelegate {

    private final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    @Override
    public ResponseEntity<Void> confirmReservation(ConfirmingReservationCommand confirmingReservationCommand) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ConfirmingReservationCommand> requestEntity = new HttpEntity<>(confirmingReservationCommand, headers);
        try {
            restTemplate.exchange("http://localhost:8082/api/v1/reservations/confirm", HttpMethod.PATCH, requestEntity, Void.class);
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiReservationResponse> createReservation(ReservationCommand reservationCommand) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReservationCommand> requestEntity = new HttpEntity<>(reservationCommand, headers);
        ResponseEntity<ApiReservationResponse> responseEntity;
        try {
            responseEntity = restTemplate.exchange("http://localhost:8082/api/v1/reservations", HttpMethod.POST, requestEntity, ApiReservationResponse.class);
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiCar> newCarListing(CarListingCommand carListingCommand) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CarListingCommand> requestEntity = new HttpEntity<>(carListingCommand, headers);
        ResponseEntity<ApiCar> responseEntity;
        try {
            responseEntity = restTemplate.exchange("http://localhost:8081/api/v1/cars", HttpMethod.POST, requestEntity, ApiCar.class);
        } catch (RestClientException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiCar> updateCarById(Integer carId, UpdateAvailabilityCar updateAvailabilityCar) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UpdateAvailabilityCar> requestEntity = new HttpEntity<>(updateAvailabilityCar, headers);
        ResponseEntity<ApiCar> responseEntity;
        try {
            responseEntity = restTemplate.exchange("http://localhost:8081/api/v1/cars/" + carId, HttpMethod.PATCH, requestEntity, ApiCar.class);
        } catch (RestClientException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @Override
    public ResponseEntity<List<ApiCar>> searchCar(String location, String carModel, BigDecimal price) {
        HttpHeaders headers = new HttpHeaders();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:8081/api/v1/cars/search");

        if(location != null && !location.isBlank()) {
            builder.queryParam("location", location);
        }
        if(carModel != null && !carModel.isBlank()) {
            builder.queryParam("carModel", carModel);
        }
        if(price != null && price.compareTo(BigDecimal.ZERO) > 0) {
            builder.queryParam("price", price);
        }

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<ApiCar>> responseEntity;
        try {
            System.out.println(builder.toUriString());
            responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity , new ParameterizedTypeReference<List<ApiCar>>() {});
        } catch (RestClientException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }
}
