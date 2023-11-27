package be.ucll.reservationservice.domain;
import jakarta.persistence.*;

import java.math.BigDecimal;
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
    private Integer billId;
    private OffsetDateTime billDueDate;
    private BigDecimal billAmount;
    private String notifyingUserMessage;

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
        this.billAmount = BigDecimal.ZERO;
        this.billDueDate = OffsetDateTime.now().plusDays(30);
        this.notifyingUserMessage = "notifyingUserMessage";
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
    public OffsetDateTime getBillDueDate() {
        return billDueDate;
    }
    public BigDecimal getBillAmount() {
        return billAmount;
    }
    public Integer getBillId() {
        return billId;
    }
    public String getNotifyingUserMessage() {
        return notifyingUserMessage;
    }
    public ReservationStatus getStatus() {
        return status;
    }

    /*
        Happy Flow:
        REGISTERED,
        VALIDATING_USER,
        RESERVING_CAR,
        CONFIRMING_RESERVATION,
        BILLING_USER,
        NOTIFYING_USER,
     */
    public void registered() {
        this.status = ReservationStatus.REGISTERED;
    }
    public void validateUser() {
        this.status = ReservationStatus.VALIDATING_USER;
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
        NO_VALID_USER,
        NO_CAR,
        DOUBLE_BOOKING,
        OWNER_DECLINES,
        BILLING_USER_FAILED,
        NOTIFYING_USER_FAILED,
     */
    public void userNotValid() {
        this.status = ReservationStatus.NO_VALID_USER;
    }
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
