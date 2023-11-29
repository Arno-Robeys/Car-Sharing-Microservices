package be.ucll.reservationservice.messaging;
import be.ucll.reservationservice.client.car.api.model.ConfirmOwnerEvent;
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

    @RabbitListener(queues = {"q.confirmed-reservation.reservation-service"})
    public void onConfirmedReservation(ConfirmOwnerEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.saga.executeSaga(event.getReservationId(), event);
    }
}
