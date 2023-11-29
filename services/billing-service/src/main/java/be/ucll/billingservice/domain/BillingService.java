package be.ucll.billingservice.domain;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@Transactional
public class BillingService {

    private final BillingRepository repository;

    @Autowired
    public BillingService(BillingRepository repository) {
        this.repository = repository;
    }

    public Billing billingUser(String email, Integer reservationId, BigDecimal amount, OffsetDateTime dueDate, Integer amountDays) {
        amount = amount.multiply(BigDecimal.valueOf(amountDays));

        Billing billing = new Billing();
        billing.setUserEmail(email);
        billing.setReservationId(reservationId);
        billing.setAmount(amount);
        billing.setIssuedDate(OffsetDateTime.now());
        billing.setDueDate(dueDate);
        billing.setStatus(Status.UNPAID);

        return repository.save(billing);
    }
}
