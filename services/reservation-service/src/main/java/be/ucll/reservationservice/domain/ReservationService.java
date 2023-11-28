package be.ucll.reservationservice.domain;

import be.ucll.reservationservice.api.model.ConfirmingReservationCommand;
import be.ucll.reservationservice.api.model.ReservationCommand;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository repository;
    private final ReservationRequestSaga requestSaga;

    @Autowired
    public ReservationService(ReservationRepository repository, ReservationRequestSaga requestSaga) {
        this.repository = repository;
        this.requestSaga = requestSaga;
    }

    public Integer registerRequest(ReservationCommand apiReservationRequest) {
        var reservation = new Reservation(apiReservationRequest.getUserId(), apiReservationRequest.getCarId(), apiReservationRequest.getStartTime(), apiReservationRequest.getEndTime());

        reservation = repository.save(reservation);
        requestSaga.executeSaga(reservation);

        return reservation.getId();
    }

    public void ownerConfirmsReservationRequest(ConfirmingReservationCommand confirmingReservationCommand) {
        requestSaga.ownerConfirmsReservation(confirmingReservationCommand);
    }
}
