package be.ucll.billingservice.domain;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@Transactional
public class BillingService {

    public Billing billingUser(String email, Integer reservationId, BigDecimal amount, OffsetDateTime dueDate) {
        return new Billing(email, reservationId, amount, OffsetDateTime.now(), dueDate, Status.UNPAID);
    }
}
