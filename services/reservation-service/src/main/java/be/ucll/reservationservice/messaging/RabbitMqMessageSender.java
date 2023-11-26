package be.ucll.reservationservice.messaging;
import be.ucll.reservationservice.api.model.ConfirmingReservationCommand;
import be.ucll.reservationservice.api.model.FinalisingReservationCommand;
import be.ucll.reservationservice.client.billing.api.model.BillCommand;
import be.ucll.reservationservice.client.billing.api.model.ReverseBillingCommand;
import be.ucll.reservationservice.client.user.api.model.NotifyingUserCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import be.ucll.reservationservice.api.model.ReservationCommand;

import java.math.BigDecimal;
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
        sendToQueue("q.car-service.reserving-car", command);
    }

    public void sendConfirmingReservationCommand(Integer reservationId) {
        var command = new ConfirmingReservationCommand();
        command.reservationId(reservationId);
        // waiting for owner to confirm but this needs to be an api call
        sendToQueue("q.reservation-service.confirming-reservation", command);
    }
    public void sendBillingUserCommand(Integer reservationId, Integer userId, BigDecimal amount, OffsetDateTime dueDate) {
        var command = new BillCommand();
        command.reservationId(reservationId);
        command.userId(userId);
        command.amount(amount);
        command.dueDate(dueDate);
        sendToQueue("q.billing-service.billing-user", command);
    }

    public void sendNotifyingUserCommand(Integer reservationId, Integer userId, String message) {
        var command = new NotifyingUserCommand();
        command.userId(userId);
        command.message(message);
        sendToQueue("q.user-service.notifying-user", command);
    }

    public void sendReverseBillingCommand(Integer reservationId, Integer billId) {
        var command = new ReverseBillingCommand();
        command.reservationId(reservationId);
        command.billId(billId);
        sendToQueue("q.billing-service.reverse-billing", command);
    }
    public void sendFinalisingReservationCommand(Integer reservationId) {
        var command = new FinalisingReservationCommand();
        command.reservationId(reservationId);
        sendToQueue("q.reservation-service.finalising-reservation", command);
    }
}