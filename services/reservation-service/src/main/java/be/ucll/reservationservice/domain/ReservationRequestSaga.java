package be.ucll.reservationservice.domain;
import be.ucll.reservationservice.api.model.ConfirmedReservationEvent;
import be.ucll.reservationservice.client.billing.api.model.BilledUserEvent;
import be.ucll.reservationservice.client.car.api.model.ReservedCarEvent;
import be.ucll.reservationservice.client.user.api.model.NotifiedUserEvent;
import be.ucll.reservationservice.messaging.RabbitMqMessageSender;
import org.springframework.stereotype.Component;

@Component
public class ReservationRequestSaga {
    private final RabbitMqMessageSender eventSender;
    private final ReservationRepository repository;

    public ReservationRequestSaga(ReservationRepository repository, RabbitMqMessageSender eventSender) {
        this.repository = repository;
        this.eventSender = eventSender;
    }

    public void executeSaga(Reservation reservation) {
        reservation.reservingCar();
        eventSender.sendReservingCarCommand(reservation.getId(), reservation.getCarId(), reservation.getStartDate(), reservation.getEndDate());
    }

    public void executeSaga(Integer id, ReservedCarEvent event) {
        Reservation reservation = repository.findById(id).orElseThrow();
        if (event.getCarNotListed()) {
            reservation.carNotListed();
            System.out.println("Car not listed");
        }
        if (event.getIsDoubleBooking()) {
            reservation.doubleBooking();
            System.out.println("Double booking");
        }
        if (!event.getCarNotListed() && !event.getIsDoubleBooking()) {
            eventSender.sendConfirmingReservationCommand(reservation.getId());
        }
    }

    public void executeSaga(Integer id, ConfirmedReservationEvent event) {
        Reservation reservation = repository.findById(id).orElseThrow();
        if (event.getOwnerDeclines()) {
            reservation.ownerDeclines();
            System.out.println("Owner declines");
        } else {
            eventSender.sendBillingUserCommand(reservation.getId(), reservation.getUserId(), reservation.getBillAmount(), reservation.getBillDueDate());
        }
    }

    public void executeSaga(Integer id, BilledUserEvent event) {
        Reservation reservation = repository.findById(id).orElseThrow();
        if (event.getBillingUserFailed()) {
            reservation.billingUserFailed();
            System.out.println("Billing user failed");
        } else {
            reservation.userBilled();
            eventSender.sendNotifyingUserCommand(reservation.getId(), reservation.getUserId(), reservation.getNotifyingUserMessage());
        }

    }

    public void executeSaga(Integer id, NotifiedUserEvent event) {
        Reservation reservation = repository.findById(id).orElseThrow();
        if (event.getNotifyingUserFailed()) {
            reservation.notifyingUserFailed();
            eventSender.sendReverseBillingCommand(reservation.getId(), reservation.getBillId());
            System.out.println("Notifying user failed");
        } else {
            reservation.userNotified();
            eventSender.sendFinalisingReservationCommand(reservation.getId());
        }
    }

    public void decline(Integer reservationId) {
        Reservation reservation = repository.findById(reservationId).orElseThrow();
        if(reservation.getStatus() == ReservationStatus.CONFIRMING_RESERVATION) {
            ConfirmedReservationEvent confirmedReservationEvent = new ConfirmedReservationEvent();
            confirmedReservationEvent.reservationId(reservationId);
            confirmedReservationEvent.ownerDeclines(true);
            executeSaga(reservationId, confirmedReservationEvent);
        } else {
            System.out.println("Decline not possible");
        }
    }

    public void accept(Integer reservationId) {
        Reservation reservation = repository.findById(reservationId).orElseThrow();
        if(reservation.getStatus() != ReservationStatus.CONFIRMING_RESERVATION) {
            System.out.println("Accept not possible");
        } else {
            ConfirmedReservationEvent confirmedReservationEvent = new ConfirmedReservationEvent();
            confirmedReservationEvent.reservationId(reservationId);
            confirmedReservationEvent.ownerDeclines(false);
            executeSaga(reservationId, confirmedReservationEvent);
        }
    }
}
