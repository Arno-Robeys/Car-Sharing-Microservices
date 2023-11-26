package be.ucll.reservationservice.domain;
import be.ucll.reservationservice.api.ReservationApiDelegate;
import be.ucll.reservationservice.api.model.ApiReservation;
import be.ucll.reservationservice.api.model.ConfirmingReservationCommand;
import be.ucll.reservationservice.api.model.ReservationCommand;
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

    //@Override
    public ResponseEntity<ApiReservation> apiV1ReservationRequestPost(ReservationCommand apiReservationRequest) {
        ApiReservation response = new ApiReservation();
        response.reservationId(reservationService.registerRequest(apiReservationRequest));
        return ResponseEntity.ok(response);
    }

    //@Override
    public ResponseEntity<Void> apiV1ReservationConfirmationPost(ConfirmingReservationCommand apiReservationConfirmation) {
        reservationService.ownerConfirmsReservationRequest(apiReservationConfirmation);
        return ResponseEntity.ok().build();
    }
}
