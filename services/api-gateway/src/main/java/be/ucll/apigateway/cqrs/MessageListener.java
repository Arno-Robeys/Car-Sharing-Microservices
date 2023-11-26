package be.ucll.apigateway.cqrs;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@Transactional
public class MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    /*@RabbitListener(queues = "q.reservation-created.api-gateway")
    public void receiveReservationCreated(String message) {
        LOGGER.info("Received message: {}", message);
    }

    @RabbitListener(queues = "q.billing-created.api-gateway")
    public void receiveBillingCreated(String message) {
        LOGGER.info("Received message: {}", message);
    }

    @RabbitListener(queues = "q.reservation-finalized.api-gateway")
    public void receiveReservationFinalized(String message) {
        LOGGER.info("Received message: {}", message);
    }*/


}
