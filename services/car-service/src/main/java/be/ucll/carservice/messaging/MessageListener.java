package be.ucll.carservice.messaging;
import be.ucll.carservice.api.model.ConfirmOwnerCommand;
import be.ucll.carservice.api.model.ConfirmOwnerEvent;
import be.ucll.carservice.domain.Car;
import be.ucll.carservice.domain.CarService;
import be.ucll.carservice.api.model.ReserveCarCommand;
import be.ucll.carservice.api.model.ReservedCarEvent;
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
    private final CarService carService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageListener(CarService carService, RabbitTemplate rabbitTemplate) {
        this.carService = carService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "q.car-service.reserving-car")
    public void receiveReservingCarCommand(ReserveCarCommand command) {
        LOGGER.info("Received command: " + command);
        Car car = carService.reserveCar(command.getCarId());

        ReservedCarEvent event = new ReservedCarEvent();
        event.setReservationId(command.getReservationId());
        event.setCarId(command.getCarId());
        event.setOwnerEmail(car.getOwnerEmail());
        event.setAvailable(car.getAvailable());

        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.reserved-car", "", event);
    }

    @RabbitListener(queues = "q.car-service.confirm-reservation-check-owner")
    public void receiveConfirmingReservationCommand(ConfirmOwnerCommand command) {
        LOGGER.info("Received command: " + command);

        Car car = carService.getCarById(command.getCarId());
        ConfirmOwnerEvent event = new ConfirmOwnerEvent();
        event.setReservationId(command.getReservationId());
        event.setOwnerEmail(command.getOwnerEmail());
        event.setCarId(command.getCarId());
        event.setAccepted(command.getAccepted());
        event.setIsOwner(command.getOwnerEmail().equals(car.getOwnerEmail()));
        event.setPrice(car.getPrice());

        this.rabbitTemplate.convertAndSend("x.confirmed-reservation", "", event);
    }
}
