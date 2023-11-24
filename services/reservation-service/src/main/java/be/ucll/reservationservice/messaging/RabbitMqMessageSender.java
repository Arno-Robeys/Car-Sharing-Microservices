package be.ucll.reservationservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import be.ucll.reservationservice.api.messaging.model.ReservationCommand;

@Component
public class RabbitMqMessageSender {
    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMqMessageSender.class);
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    private void sendToQueue(String queue, Object message) {
        LOGGER.info("Sending message: " + message);
        this.rabbitTemplate.convertAndSend(queue, message);
    }


    /*
    public void sendReservingCarCommand(Integer reservationId, Integer carId) {
        var command = new ReservationCommand();
        command.reservationId(reservationId);
        command.carId(carId);
        sendToQueue("q.reservation-service.reserving-car", command);
    }
     */

}
