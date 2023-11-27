package be.ucll.reservationservice.web;
import be.ucll.reservationservice.api.ReservationApiDelegate;
import be.ucll.reservationservice.api.model.ApiReservation;
import be.ucll.reservationservice.api.model.ConfirmingReservationCommand;
import be.ucll.reservationservice.api.model.ReservationCommand;
import be.ucll.reservationservice.domain.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationController implements ReservationApiDelegate {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public ResponseEntity<ApiReservation> createReservation(ReservationCommand apiReservationRequest) {
        ApiReservation response = new ApiReservation();
        response.reservationId(reservationService.registerRequest(apiReservationRequest));
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<ApiReservation> getReservationById(Integer reservationId) {
        return null;
    }

    /*@Override
    public ResponseEntity<Void> apiV1ReservationConfirmationPost(ConfirmingReservationCommand apiReservationConfirmation) {
        reservationService.ownerConfirmsReservationRequest(apiReservationConfirmation);
        return ResponseEntity.ok().build();
    }*/
}
