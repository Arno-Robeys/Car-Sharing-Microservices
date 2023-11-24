package be.ucll.billingservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer carId;
    private Integer reservationId;
    private Double amount;
    private LocalDateTime issuedDate;
    private LocalDateTime dueDate;
    /*
     status:
          type: string
          enum: [PAID, UNPAID, OVERDUE]
     */
    private String status;

    public Billing(
            Integer userId,
            Integer carId,
            Integer reservationId,
            Double amount,
            LocalDateTime issuedDate,
            LocalDateTime dueDate,
            String status
    ) {
        this.userId = userId;
        this.carId = carId;
        this.reservationId = reservationId;
        this.amount = amount;
        this.issuedDate = issuedDate;
        this.dueDate = dueDate;
        this.status = status;
    }
    public Billing() {

    }
}
