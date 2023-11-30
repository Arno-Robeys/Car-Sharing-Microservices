package be.ucll.reservationservice.domain;
import be.ucll.reservationservice.api.model.ConfirmingReservationCommand;
import be.ucll.reservationservice.client.billing.api.model.BilledUserEvent;
import be.ucll.reservationservice.client.car.api.model.ConfirmOwnerEvent;
import be.ucll.reservationservice.client.car.api.model.ReservedCarEvent;
import be.ucll.reservationservice.client.user.api.model.ValidatedUserEvent;
import be.ucll.reservationservice.messaging.RabbitMqMessageSender;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class ReservationRequestSaga {
    private final RabbitMqMessageSender eventSender;
    private final ReservationRepository repository;

    public ReservationRequestSaga(ReservationRepository repository, RabbitMqMessageSender eventSender) {
        this.repository = repository;
        this.eventSender = eventSender;
    }

    public void executeSaga(Reservation reservation) {
        reservation.validateUser();
        eventSender.sendValidateUserCommand(reservation.getId(), reservation.getUserEmail());
    }

    public void executeSage(Integer id, ValidatedUserEvent event) {
        Reservation reservation = repository.findById(id).orElseThrow();
        if (Boolean.FALSE.equals(event.getUserValid())) {
            reservation.userNotValid();
            eventSender.sendEmailNotificationCommand(event.getEmail(), "This email/user doesn't exist in our system");
        } else {
            reservation.reservingCar();
            eventSender.sendReservingCarCommand(reservation.getId(), reservation.getCarId());
        }
    }



    public void executeSaga(Integer id, ReservedCarEvent event) {
        Reservation reservation = repository.findById(id).orElseThrow();

        if(Boolean.TRUE.equals(event.getAvailable())) {
            List<Reservation> reservations = repository.getReservationsForCarOverlapping(reservation.getId(), event.getCarId(), reservation.getStartDate(), reservation.getEndDate());
            if(reservations.isEmpty()) {
                reservation.confirmingReservation();
                eventSender.sendEmailNotificationCommand(reservation.getUserEmail(), "Owner needs to confirm reservation");
                eventSender.sendEmailNotificationCommand(event.getOwnerEmail(), "You have a reservation request for your car " + event.getCarId() + " from " + reservation.getStartDate() + " to " + reservation.getEndDate() + " for " + reservation.getUserEmail() + ". Please confirm or decline the reservation with the following id: " + reservation.getId());
            } else {
                reservation.doubleBooking();
                eventSender.sendEmailNotificationCommand(reservation.getUserEmail(), "This car is already booked");
            }
        } else {
            reservation.carNotListed();
            eventSender.sendEmailNotificationCommand(reservation.getUserEmail(), "Car not listed/available");
        }
    }

    public void executeSaga(Integer id, ConfirmOwnerEvent event) {
        Reservation reservation = repository.findById(id).orElseThrow();
        if(Boolean.TRUE.equals(event.getIsOwner())) {
            if (Boolean.TRUE.equals(event.getAccepted())) {
                reservation.reservationConfirmed();
                Integer amountDays = 1+ (Math.toIntExact(ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate())));
                eventSender.sendBillingUserCommand(reservation.getId(), reservation.getUserEmail(), event.getPrice(), reservation.getBillDueDate(), amountDays);
            } else {
                reservation.ownerDeclines();
                eventSender.sendEmailNotificationCommand(reservation.getUserEmail(), "Owner declined reservation");
            }
        } else eventSender.sendEmailNotificationCommand(event.getOwnerEmail(), "You are not the owner of this car");
    }

    public void executeSaga(Integer id, BilledUserEvent event) {
        Reservation reservation = repository.findById(id).orElseThrow();
        reservation.completed();
        eventSender.sendEmailNotificationCommand(event.getUserEmail(), "Your bill is " + event.getBillAmount() + " and is due on " + reservation.getBillDueDate());
    }

    public void ownerConfirmsReservation(ConfirmingReservationCommand confirmingReservationCommand) {
        Reservation reservation = repository.findById(confirmingReservationCommand.getReservationId()).orElseThrow();
        if(reservation.getStatus() == ReservationStatus.CONFIRMING_RESERVATION) {
            eventSender.sendConfirmingReservationCommand(confirmingReservationCommand.getReservationId(), confirmingReservationCommand.getOwnerEmail(), reservation.getCarId(), confirmingReservationCommand.getAccepted());
        } else eventSender.sendEmailNotificationCommand(reservation.getUserEmail(), "Reservation can't be confirmed at this moment (status: " + reservation.getStatus() + ")");
    }
}
