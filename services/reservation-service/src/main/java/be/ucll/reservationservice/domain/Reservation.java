package be.ucll.reservationservice.domain;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer carId;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public Reservation(
            Integer userId,
            Integer carId,
            OffsetDateTime startDate,
            OffsetDateTime endDate
    ) {
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = ReservationStatus.REGISTERED;
    }

    public Reservation() {

    }

    public Integer getId() {
        return id;
    }
    public Integer getCarId() {
        return carId;
    }
    public Integer getUserId() {
        return userId;
    }
    public OffsetDateTime getStartDate() {
        return startDate;
    }
    public OffsetDateTime getEndDate() {
        return endDate;
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
    public void carNotListed() {
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