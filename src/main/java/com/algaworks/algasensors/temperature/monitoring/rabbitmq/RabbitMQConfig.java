package com.algaworks.algasensors.temperature.monitoring.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    public static final String TEMPERATURE_MONITORING_PROCESS_TEMPERATURE_V_1_Q = "temperature-monitoring.process-temperature.v1.q";
    public static final String ALERTING_MONITORING_PROCESS_TEMPERATURE_V_1_Q = "alerting-monitoring.process-temperature.v1.q";

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    //Consumer
    @Bean
    public Queue queueProcessTemperature() {
        return QueueBuilder.durable(TEMPERATURE_MONITORING_PROCESS_TEMPERATURE_V_1_Q).build();
    }

    @Bean
    public Queue queueAlerting() {
        return QueueBuilder.durable(ALERTING_MONITORING_PROCESS_TEMPERATURE_V_1_Q).build();
    }

    //Referencia o consumer ao producer
    @Bean
    public Binding bindingProcessTemperature() {
        return BindingBuilder.bind(queueProcessTemperature()).to(exchange());
    }

    //Referencia o consumer ao producer
    @Bean
    public Binding bindingAlerting() {
        return BindingBuilder.bind(queueAlerting()).to(exchange());
    }

    //Serializar e dessearializar os objetos do payload
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    //Producer declarado no temperature-processing
    public FanoutExchange exchange() {
        return ExchangeBuilder.fanoutExchange("temperature-processing.temperature-received.v1.e").build();
    }

}
