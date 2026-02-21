package com.algaworks.algasensors.temperature.monitoring.rabbitmq;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.service.SensorAlertService;
import com.algaworks.algasensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.algaworks.algasensors.temperature.monitoring.rabbitmq.RabbitMQConfig.ALERTING_MONITORING_PROCESS_TEMPERATURE_V_1_Q;
import static com.algaworks.algasensors.temperature.monitoring.rabbitmq.RabbitMQConfig.TEMPERATURE_MONITORING_PROCESS_TEMPERATURE_V_1_Q;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;
    private final SensorAlertService sensorAlertService;

    //Anotando o lister e indicando o consumer - pode ser mais de uma, mas, recomenda-se, um listener por consumer
    //Exemplo contempla o recebimento de cabeçalho, mas não é obrigatório, depende da necessidade.
   /* @RabbitListener(queues = TEMPERATURE_MONITORING_PROCESS_TEMPERATURE_V_1_Q)
    @SneakyThrows
    public void handle(
            @Payload TemperatureLogOutput temperatureLogOutput,
            @Headers Map<String, Object> headers) {
        TSID sensorId = temperatureLogOutput.getSensorId();
        Double temperature = temperatureLogOutput.getValue();
        log.info(headers.toString());
        log.info("Received Temperature Log Output from RabbitMQ Listener: {}", sensorId + " | " + temperature);
    }*/

    @RabbitListener(queues = TEMPERATURE_MONITORING_PROCESS_TEMPERATURE_V_1_Q)
    @SneakyThrows
    public void handleProcessingTemperature(
            @Payload TemperatureLogOutput temperatureLogOutput
    ) {
        temperatureMonitoringService.processTemperature(temperatureLogOutput);
    }


    @RabbitListener(queues = ALERTING_MONITORING_PROCESS_TEMPERATURE_V_1_Q)
    @SneakyThrows
    public void handleAlerting(
            @Payload TemperatureLogOutput temperatureLogOutput
    ) {
        sensorAlertService.handlerAlert(temperatureLogOutput);
    }


}
