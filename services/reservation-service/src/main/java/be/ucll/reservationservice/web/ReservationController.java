package be.ucll.reservationservice.web;

import be.ucll.reservationservice.api.ReservationApiDelegate;
import be.ucll.reservationservice.api.model.ApiReservation;
import be.ucll.reservationservice.api.model.ReservationCommand;
import org.springframework.http.ResponseEntity;

public class ReservationController implements ReservationApiDelegate {

    @Override
    public ResponseEntity<ApiReservation> createReservation(ReservationCommand reservationCommand) {
        return null;
    }

    @Override
    public ResponseEntity<ApiReservation> getReservationById(Integer reservationId) {
        return null;
    }

}
