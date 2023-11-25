package be.ucll.reservationservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import be.ucll.reservationservice.api.model.ReservationCommand;
import java.time.OffsetDateTime;

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

    public void sendReservingCarCommand(Integer userId, Integer carId, OffsetDateTime startTime, OffsetDateTime endTime) {
        var command = new ReservationCommand();
        command.userId(userId);
        command.carId(carId);
        command.startTime(startTime);
        command.endTime(endTime);
        sendToQueue("q.reservation-service.reserving-car", command);
    }

}
