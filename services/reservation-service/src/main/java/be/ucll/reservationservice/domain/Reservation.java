package be.ucll.reservationservice.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    private String userEmail;
    private Integer carId;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private Integer billId;
    private OffsetDateTime billDueDate;
    private BigDecimal billAmount;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public Reservation(
            String userEmail,
            Integer carId,
            OffsetDateTime startDate,
            OffsetDateTime endDate
    ) {
        this.userEmail = userEmail;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.billAmount = BigDecimal.ZERO;
        this.billDueDate = startDate.minusHours(2);
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
    public String getUserEmail() {
        return userEmail;
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
    /*
        Failure States:
        NO_VALID_USER,
        NO_CAR,
        DOUBLE_BOOKING,
        OWNER_DECLINES,
        BILLING_USER_FAILED,
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

    public void completed() {
        this.status = ReservationStatus.COMPLETED;
    }
    public void listingRemoved(){
        this.status = ReservationStatus.LISTING_REMOVED;
    }

}
