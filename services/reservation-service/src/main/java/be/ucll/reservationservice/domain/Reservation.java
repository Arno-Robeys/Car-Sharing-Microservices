package be.ucll.reservationservice.domain;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public Reservation() {
        this.status = ReservationStatus.RESERVING_CAR;
    }

    public Integer getId() {
        return id;
    }
    /*
        Happy Flow:
        REGISTERED,
        RESERVING_CAR,
        CONFIRMING_RESERVATION,
        BILLING_USER,
        NOTIFYING_USER,
     */
    public void registered() {
        this.status = ReservationStatus.REGISTERED;
    }
    public void reservingCar() {
        this.status = ReservationStatus.RESERVING_CAR;
    }
    public void confirmingReservation() {
        this.status = ReservationStatus.CONFIRMING_RESERVATION;
    }
    public void reservationConfirmed() {
        this.status = ReservationStatus.BILLING_USER;
    }
    public void userBilled(){
        this.status = ReservationStatus.NOTIFYING_USER;
    }
    public void userNotified(){
        this.status = ReservationStatus.COMPLETED;
    }
    /*
        Failure States:
        NO_CAR,
        DOUBLE_BOOKING,
        OWNER_DECLINES,
        BILLING_USER_FAILED,
        NOTIFYING_USER_FAILED,
     */
    public void noCarAvailable() {
        this.status = ReservationStatus.NO_CAR;
    }
    public void doubleBooking() {
        this.status = ReservationStatus.DOUBLE_BOOKING;
    }
    public void ownerDeclines() {
        this.status = ReservationStatus.OWNER_DECLINES;
    }
    public void billingUserFailed() {
        this.status = ReservationStatus.BILLING_USER_FAILED;
    }
    public void notifyingUserFailed() {
        this.status = ReservationStatus.NOTIFYING_USER_FAILED;
    }
    /*
        Final States:
        COMPLETED,
        CANCELED,
        LISTING_REMOVED

        Completed is done after notifying user
    */
    public void canceled() {
        this.status = ReservationStatus.CANCELED;
    }
    public void listingRemoved(){
        this.status = ReservationStatus.LISTING_REMOVED;
    }
}
