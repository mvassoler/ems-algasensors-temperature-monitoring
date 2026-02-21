package com.algaworks.algasensors.temperature.monitoring.domain.service;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SensorAlertService {

    private final SensorAlertRepository sensorAlertRepository;

    public void handlerAlert(TemperatureLogOutput temperatureLogOutput) {
        sensorAlertRepository.findById(new SensorId(temperatureLogOutput.getSensorId()))
                .ifPresentOrElse(
                        sensorMonitoring -> {
                            if (sensorMonitoring.getMaxTemperature() != null &&
                                    temperatureLogOutput.getValue().compareTo(sensorMonitoring.getMaxTemperature()) >= 0
                            ) {
                                log.info("Temperature alert for sensor {} with value {}", temperatureLogOutput.getSensorId(), temperatureLogOutput.getValue());

                            } else if (sensorMonitoring.getMinTemperature() != null &&
                                    temperatureLogOutput.getValue().compareTo(sensorMonitoring.getMinTemperature()) >= 0) {
                                log.info("Temperature alert for sensor {} with value {}", temperatureLogOutput.getSensorId(), temperatureLogOutput.getValue());
                            } else {
                                log.info("No sensor found with id {}", temperatureLogOutput.getSensorId());
                            }
                        },
                        () -> {
                            log.info("No sensor found with id {}", temperatureLogOutput.getSensorId());
                        }
                );

    }

}
