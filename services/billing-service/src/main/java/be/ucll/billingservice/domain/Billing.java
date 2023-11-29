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

    public Billing(
            String userEmail,
            Integer reservationId,
            BigDecimal amount,
            OffsetDateTime issuedDate,
            OffsetDateTime dueDate,
            Status status
    ) {
        this.userEmail = userEmail;
        this.reservationId = reservationId;
        this.amount = amount;
        this.issuedDate = issuedDate;
        this.dueDate = dueDate;
        this.status = status;
    }
    public Billing() {

    }
}
