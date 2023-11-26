package be.ucll.reservationservice.messaging;

import be.ucll.billingservice.api.model.BillCommand;
import be.ucll.billingservice.api.model.ReverseBillingCommand;
import be.ucll.userservice.api.model.NotifyingUserCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
// import be.ucll.reservationservice.api.model.ReservationCommand;

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
    public void sendReservingCarCommand(Integer userId, Integer carId, OffsetDateTime startTime, OffsetDateTime endTime) {
        var command = new ReservationCommand();
        command.userId(userId);
        command.carId(carId);
        command.startTime(startTime);
        command.endTime(endTime);
        sendToQueue("q.reservation-service.reserving-car", command);
    }*/
    /*
    public void sendConfirmingReservationCommand(Integer id) {
        var command = new ConfirmingReservationCommand();
        sendToQueue("q.reservation-service.confirming-reservation", command);
    }*/

    public void sendBillingUserCommand(Integer id) {
        var command = new BillCommand();
        sendToQueue("q.reservation-service.billing-user", command);
    }

    public void sendNotifyingUserCommand(Integer id) {
        var command = new NotifyingUserCommand();
        sendToQueue("q.reservation-service.notifying-user", command);
    }

    public void sendReverseBillingCommand(Integer id) {
        var command = new ReverseBillingCommand();
        sendToQueue("q.reservation-service.reverse-billing", command);
    }
/*
    public void sendFinalisingReservationCommand(Integer id) {
        var command = new FinalisingReservationCommand();
        sendToQueue("q.reservation-service.finalising-reservation", command);
    }*/
}
