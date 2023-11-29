package be.ucll.notificationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;
import be.ucll.notificationservice.api.model.SendEmailCommand;

@Component
@Transactional
public class MessageListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    @RabbitListener(queues = {"q.notification-service.send-email-notification"})
    public void onSendEmailNotification(SendEmailCommand command) {
        LOGGER.info("Sending email with text " + command.getMessage() + " to recipient "+ command.getRecipient() + ".");
    }
}
