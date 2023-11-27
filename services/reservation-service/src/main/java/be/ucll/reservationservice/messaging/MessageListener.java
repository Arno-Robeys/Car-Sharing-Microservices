package be.ucll.reservationservice.messaging;
import be.ucll.reservationservice.client.user.api.model.ValidatedUserEvent;
import be.ucll.reservationservice.client.car.api.model.ReservedCarEvent;
import be.ucll.reservationservice.domain.ReservationRequestSaga;
import be.ucll.reservationservice.domain.ReservationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Transactional
public class MessageListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);
    private final ReservationRequestSaga saga;
    private final RabbitTemplate rabbitTemplate;
    private final ReservationService reservationService;

    @Autowired
    public MessageListener(ReservationRequestSaga saga , ReservationService reservationService, RabbitTemplate rabbitTemplate) {
        this.saga = saga;
        this.reservationService = reservationService;
        this.rabbitTemplate = rabbitTemplate;
    }
    @RabbitListener(queues = {"q.reserved-car.reservation-service"})
    public void onReservedCar(ReservedCarEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.saga.executeSaga(event.getReservationId(), event);
    }

    @RabbitListener(queues = {"q.validated-user.reservation-service"})
    public void onUserValidated(ValidatedUserEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.saga.executeSage(event.getReservationId(), event);
    }

    //TODO: make queue for this in RabbitMqConfig
    /*@RabbitListener(queues = {"q.confirmed-reservation.reservation-service"})
    public void onConfirmedReservation(ConfirmedReservationEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.saga.executeSaga(event.getReservationId(), event);
    }

    @RabbitListener(queues = {"q.billed-user.reservation-service"})
    public void onBilledUser(BilledUserEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.saga.executeSaga(event.getReservationId(), event);
    }

    /*@RabbitListener(queues = {"q.notified-user.reservation-service"})
    public void onNotifiedUser(NotifiedUserEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.saga.executeSaga(event.getReservationId(), event);
    }

    @RabbitListener(queues = {"q.reservation-service.reserving-car"})
    public void onReservingCar(ReservationCommand command) {
        LOGGER.info("Received command: " + command);

        Reservation reservation = reservationService.reserveCar(command.getUserId(), command.getCarId(), command.getStartTime(), command.getEndTime());
        ReservedCarEvent event = new ReservedCarEvent();
        event.reservationId(reservation.getId());
        event.carId(reservation.getCarId());
        event.ownerId(reservation.getUserId());
        event.carNotListed(reservation.getStatus() == ReservationStatus.NO_CAR);
        event.isDoubleBooking(reservation.getStatus() == ReservationStatus.DOUBLE_BOOKING);
        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.reserved-car", "", event);
    }*/
}
