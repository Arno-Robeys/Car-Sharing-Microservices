package be.ucll.reservationservice.messaging;
import be.ucll.reservationservice.api.model.FinalisingReservationCommand;
import be.ucll.reservationservice.client.billing.api.model.BillCommand;
import be.ucll.reservationservice.client.billing.api.model.ReverseBillingCommand;
import be.ucll.reservationservice.client.car.api.model.ConfirmOwnerCommand;
import be.ucll.reservationservice.client.car.api.model.ReserveCarCommand;
import be.ucll.reservationservice.client.user.api.model.NotifyingUserCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import be.ucll.reservationservice.client.user.api.model.ValidateUserCommand;

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

    public void sendValidateUserCommand(Integer reservationId, Integer userId) {
        var command = new ValidateUserCommand();
        command.userId(userId);
        command.reservationId(reservationId);
        sendToQueue("q.user-service.validate-user", command);
    }

    public void sendReservingCarCommand(Integer reservationId, Integer userId, Integer carId, OffsetDateTime startTime, OffsetDateTime endTime) {
        var command = new ReserveCarCommand();
        command.reservationId(reservationId);
        command.carId(carId);
        sendToQueue("q.reservation-service.reserving-car", command);
    }

    public void sendConfirmingReservationCommand(Integer reservationId, Integer userId, Integer carId, Boolean accepted) {
        var command = new ConfirmOwnerCommand();
        command.reservationId(reservationId);
        command.ownerId(userId);
        command.carId(carId);
        command.accepted(accepted);
        sendToQueue("q.car-service.confirm-reservation-check-owner", command);
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