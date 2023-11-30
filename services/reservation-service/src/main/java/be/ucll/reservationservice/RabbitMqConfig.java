package be.ucll.reservationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Jackson2JsonMessageConverter converter() {
        ObjectMapper mapper =
                new ObjectMapper()
                        .registerModule(new ParameterNamesModule())
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat());

        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter converter, CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

    @Bean
    public Declarables createValidateUserQueue(){
        return new Declarables(new Queue("q.user-service.validate-user"));
    }

    @Bean
    public Declarables createValidatedUserExchange() {
        return new Declarables(
                new FanoutExchange("x.validated-user"),
                new Queue("q.validated-user.reservation-service"),
                new Binding("q.validated-user.reservation-service", Binding.DestinationType.QUEUE, "x.validated-user", "validated-user.reservation-service", null));
    }


    @Bean
    public Declarables createReservingCarQueue(){
        return new Declarables(new Queue("q.car-service.reserving-car"));
    }

    @Bean
    public Declarables createReservedCarExchange() {
        return new Declarables(
                new FanoutExchange("x.reserved-car"),
                new Queue("q.reserved-car.reservation-service"),
                new Binding("q.reserved-car.reservation-service", Binding.DestinationType.QUEUE, "x.reserved-car", "reserved-car.reservation-service", null));
    }

    @Bean
    public Declarables createConfirmingReservationQueue(){
        return new Declarables(new Queue("q.car-service.confirm-reservation-check-owner"));
    }

    @Bean
    public Declarables createConfirmingReservationExchange(){
        return new Declarables(
                new FanoutExchange("x.confirmed-reservation"),
                new Queue("q.confirmed-reservation.reservation-service"),
                new Binding("q.confirmed-reservation.reservation-service", Binding.DestinationType.QUEUE, "x.confirmed-reservation", "confirmed-reservation.reservation-service", null));
    }

    @Bean
    public Declarables createBillingUserQueue(){
        return new Declarables(new Queue("q.billing-service.billing-user"));
    }

    @Bean
    public Declarables createBillingUserExchange(){
        return new Declarables(
                new FanoutExchange("x.billed-user"),
                new Queue("q.billed-user.reservation-service"),
                new Binding("q.billed-user.reservation-service", Binding.DestinationType.QUEUE, "x.billed-user", "billed-user.reservation-service", null));
    }

    @Bean
    public Declarables createSendEmailNotificationQueue(){
        return new Declarables(new Queue("q.notification-service.send-email-notification"));
    }
}