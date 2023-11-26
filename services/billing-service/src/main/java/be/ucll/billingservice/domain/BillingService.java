package be.ucll.billingservice.domain;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@Transactional
public class BillingService {
    /*
    private Integer carId;
    private Integer reservationId;
    private Double amount;
    private LocalDateTime issuedDate;
    private LocalDateTime dueDate;
     */
    public Billing billingUser(Integer userId, Integer reservationId, BigDecimal amount, OffsetDateTime dueDate) {
        return new Billing(userId, reservationId, amount, OffsetDateTime.now(), dueDate, "UNPAID");
    }
}
