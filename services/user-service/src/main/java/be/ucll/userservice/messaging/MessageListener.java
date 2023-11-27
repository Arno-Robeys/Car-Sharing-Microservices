package be.ucll.userservice.messaging;

import be.ucll.userservice.domain.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import be.ucll.userservice.api.model.ValidatedUserEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;
import be.ucll.userservice.api.model.ValidateUserCommand;

@Component
@Transactional
public class MessageListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);
    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageListener(UserService userService, RabbitTemplate rabbitTemplate) {
        this.userService = userService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "q.user-service.validate-user")
    public void validateUser(ValidateUserCommand command) {
        LOGGER.info("Validating user command: " + command);
        var user = userService.validateUser(command.getUserId());

        ValidatedUserEvent event = new ValidatedUserEvent();
        event.setReservationId(command.getReservationId());
        event.setUserValid(true);
        event.setUserId(command.getUserId());

        this.rabbitTemplate.convertAndSend("x.validated-user", "validated-user.reservation-service", event);
    }
}
