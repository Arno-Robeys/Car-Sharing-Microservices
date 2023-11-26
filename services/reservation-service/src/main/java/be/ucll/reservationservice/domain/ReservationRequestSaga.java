package be.ucll.reservationservice.domain;
import be.ucll.billingservice.api.model.BilledUserEvent;
import be.ucll.carservice.api.model.ReservedCarEvent;
import be.ucll.reservationservice.api.model.ConfirmedReservationEvent;
import be.ucll.reservationservice.messaging.RabbitMqMessageSender;
import be.ucll.userservice.api.model.NotifiedUserEvent;
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
        if(event.getCarNotListed()) {
            reservation.carNotListed();
            System.out.println("Car not listed");
        }
        if(event.getIsDoubleBooking()){
            reservation.doubleBooking();
            System.out.println("Double booking");
        }
        if(!event.getCarNotListed() && !event.getIsDoubleBooking()){
            eventSender.sendConfirmingReservationCommand(reservation.getId());
        }
    }
    public void executeSaga(Integer id, ConfirmedReservationEvent event) {
        Reservation reservation = repository.findById(id).orElseThrow();
        if(event.getOwnerDeclines()) {
            reservation.ownerDeclines();
            System.out.println("Owner declines");
        } else {
            eventSender.sendBillingUserCommand(reservation.getId());
        }
    }
    public void executeSaga(Integer id, BilledUserEvent event) {
        Reservation reservation = repository.findById(id).orElseThrow();
        if(event.getBillingUserFailed()) {
            reservation.billingUserFailed();
            System.out.println("Billing user failed");
        } else {
            reservation.userBilled();
            eventSender.sendNotifyingUserCommand(reservation.getId());
        }

    }
    public void executeSaga(Integer id, NotifiedUserEvent event) {
        Reservation reservation = repository.findById(id).orElseThrow();
        if(event.getNotifyingUserFailed()) {
            reservation.notifyingUserFailed();
            eventSender.sendReverseBillingCommand(reservation.getId());
            System.out.println("Notifying user failed");
        } else {
            reservation.userNotified();
            eventSender.sendFinalisingReservationCommand(reservation.getId());
        }
    }
}
