package be.ucll.reservationservice.domain;

import be.ucll.reservationservice.api.model.ConfirmingReservationCommand;
import be.ucll.reservationservice.api.model.ReservationCommand;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

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

    public void ownerConfirmsReservationRequest(ConfirmingReservationCommand apiReservationConfirmation) {
        if (apiReservationConfirmation.getOwnerDeclines()) {
            requestSaga.decline(apiReservationConfirmation.getReservationId());
        } else {
            requestSaga.accept(apiReservationConfirmation.getReservationId());
        }
    }
    public Reservation reserveCar(Integer userId, Integer carId, OffsetDateTime startDate, OffsetDateTime endDate) {
        List<Reservation> reservations = repository.getReservationsForCarOverlapping(carId, startDate, endDate);
        if (!reservations.isEmpty()) {
            Reservation reservation = new Reservation();
            reservation.doubleBooking();
            return reservation;
        } else {
            return repository.save(new Reservation(userId,carId,startDate, endDate));
        }
    }
}
