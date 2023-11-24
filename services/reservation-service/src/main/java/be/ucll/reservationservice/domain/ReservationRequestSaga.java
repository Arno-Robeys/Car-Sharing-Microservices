package be.ucll.reservationservice.domain;

import be.ucll.reservationservice.messaging.RabbitMqMessageSender;
import org.springframework.stereotype.Component;

@Component
public class ReservationRequestSaga {
    private final RabbitMqMessageSender eventSender;
    private final ReservationRepository repository;

    public ReservationRequestSaga(ReservationRepository repository, RabbitMqMessageSender eventSender) {
        this.repository = repository;
        this.eventSender = eventSender;
    }
    public void executeSaga(Reservation reservation) {
        reservation.reservingCar();
        eventSender.sendReservingCarCommand(reservation.getId(), reservation.getCarId());
    }
}
