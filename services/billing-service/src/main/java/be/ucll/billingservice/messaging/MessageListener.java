package be.ucll.billingservice.messaging;


import be.ucll.billingservice.api.model.BillCommand;
import be.ucll.billingservice.api.model.BilledUserEvent;
import be.ucll.billingservice.domain.Billing;
import be.ucll.billingservice.domain.BillingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;

@Component
@Transactional
public class MessageListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);
    private final BillingService billingService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageListener(BillingService billingService, RabbitTemplate rabbitTemplate) {
        this.billingService = billingService;
        this.rabbitTemplate = rabbitTemplate;
    }
    @RabbitListener(queues = {"q.billing-service.billing-user"})
    public void onBillingUser(BillCommand command) {
        LOGGER.info("Received command: " + command);

        Billing billing = billingService.billingUser(Integer.parseInt(command.getUserId()), Integer.parseInt(command.getReservationId()), command.getAmount(), command.getDueDate());
        BilledUserEvent event = new BilledUserEvent();

        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.billed-user", "", event);
    }
}
