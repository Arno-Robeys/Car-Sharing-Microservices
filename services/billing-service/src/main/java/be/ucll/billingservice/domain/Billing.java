package be.ucll.billingservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    private String userEmail;
    private Integer reservationId;
    private BigDecimal amount;
    private OffsetDateTime issuedDate;
    private OffsetDateTime dueDate;
    /*
     status:
          type: string
          enum: [PAID, UNPAID, OVERDUE]
     */
    private Status status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OffsetDateTime getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(OffsetDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }

    public OffsetDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(OffsetDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
