package be.ucll.billingservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer userId;
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
            Integer userId,
            Integer reservationId,
            BigDecimal amount,
            OffsetDateTime issuedDate,
            OffsetDateTime dueDate,
            Status status
    ) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.amount = amount;
        this.issuedDate = issuedDate;
        this.dueDate = dueDate;
        this.status = status;
    }
    public Billing() {

    }
}
