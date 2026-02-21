package com.algaworks.algasensors.temperature.monitoring.domain.service;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLog;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLogId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonotoringRepository;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemperatureMonitoringService {

    private final SensorMonotoringRepository sensorMonotoringRepository;
    private final TemperatureLogRepository temperatureLogRepository;


    private void handleTemperatureLogOutput(TemperatureLogOutput temperatureLogOutput, SensorMonitoring sensor) {
        if (sensor.isEnabled()) {
            sensor.setLastTemperature(temperatureLogOutput.getValue());
            sensor.setUpdatedAt(OffsetDateTime.now());
            sensorMonotoringRepository.save(sensor);
            TemperatureLog temperatureLog = TemperatureLog.builder()
                    .id(new TemperatureLogId(temperatureLogOutput.getId()))
                    .value(temperatureLogOutput.getValue())
                    .registeredAt(temperatureLogOutput.getRegisteredAt())
                    .sensorId(new SensorId(temperatureLogOutput.getSensorId())).build();
            temperatureLogRepository.save(temperatureLog);
            log.info("Temperature Log Output processed: {}", temperatureLogOutput.getSensorId());
        } else {
            logIgnoredTemperature(temperatureLogOutput);
        }
    }


    private void logIgnoredTemperature(TemperatureLogOutput temperatureLogOutput) {
        log.info("Temperature Log Output ignored: {}", temperatureLogOutput.getSensorId());
    }

    @Transactional
    public void processTemperature(TemperatureLogOutput temperatureLogOutput) {

        sensorMonotoringRepository.findById(new SensorId(temperatureLogOutput.getSensorId()).getValue())
                .ifPresentOrElse(
                        sensorMonitoring -> this.handleTemperatureLogOutput(temperatureLogOutput, sensorMonitoring),
                        () -> logIgnoredTemperature(temperatureLogOutput)
                );
    }

}
