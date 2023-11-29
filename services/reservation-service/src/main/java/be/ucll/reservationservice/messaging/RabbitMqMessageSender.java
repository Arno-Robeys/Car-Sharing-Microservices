package be.ucll.reservationservice.messaging;
import be.ucll.notificationservice.client.user.api.model.SendEmailCommand;
import be.ucll.reservationservice.api.model.FinalisingReservationCommand;
import be.ucll.reservationservice.client.billing.api.model.BillCommand;
import be.ucll.reservationservice.client.billing.api.model.ReverseBillingCommand;
import be.ucll.reservationservice.client.car.api.model.ConfirmOwnerCommand;
import be.ucll.reservationservice.client.car.api.model.ReserveCarCommand;
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

    public void sendValidateUserCommand(Integer reservationId, String email) {
        var command = new ValidateUserCommand();
        command.setEmail(email);
        command.setReservationId(reservationId);
        sendToQueue("q.user-service.validate-user", command);
    }

    public void sendReservingCarCommand(Integer reservationId, Integer carId) {
        var command = new ReserveCarCommand();
        command.reservationId(reservationId);
        command.carId(carId);
        sendToQueue("q.reservation-service.reserving-car", command);
    }

    public void sendConfirmingReservationCommand(Integer reservationId, String email, Integer carId, Boolean accepted) {
        var command = new ConfirmOwnerCommand();
        command.reservationId(reservationId);
        command.ownerEmail(email);
        command.carId(carId);
        command.accepted(accepted);
        sendToQueue("q.car-service.confirm-reservation-check-owner", command);
    }

    public void sendBillingUserCommand(Integer reservationId, String email, BigDecimal amount, OffsetDateTime dueDate, Integer amountDays) {
        var command = new BillCommand();
        command.setReservationId(reservationId);
        command.setUserEmail(email);
        command.setAmount(amount);
        command.setDueDate(dueDate);
        command.setAmountDays(amountDays);
        sendToQueue("q.billing-service.billing-user", command);
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

    public void sendEmailNotificationCommand(String recipient, String message) {
        var command = new SendEmailCommand();
        command.recipient(recipient);
        command.message(message);
        sendToQueue("q.notification-service.send-email-notification", command);
    }
}