package com.algaworks.algasensors.temperature.monitoring.api.controller;

import com.algaworks.algasensors.temperature.monitoring.api.model.SensorMonitoringOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonotoringRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensors/{sensorId}/monitoring")
@RequiredArgsConstructor
public class SensorMonitoringController {

    private final SensorMonotoringRepository sensorMonotoringRepository;

    @GetMapping
    public SensorMonitoringOutput getDetail(@PathVariable TSID sensorId) {
        SensorMonitoring sensorMonitoring = getSensorMonitoring(sensorId);

        return SensorMonitoringOutput.builder()
                .id(sensorMonitoring.getId())
                .lastTemperature(sensorMonitoring.getLastTemperature())
                .updatedAt(sensorMonitoring.getUpdatedAt())
                .enabled(sensorMonitoring.getEnabled())
                .build();
    }


    @PutMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable TSID sensorId) {
        SensorMonitoring sensorMonitoring = getSensorMonitoring(sensorId);
        sensorMonitoring.setEnabled(true);
        sensorMonotoringRepository.saveAndFlush(sensorMonitoring);
    }

    @DeleteMapping("/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable TSID sensorId) {
        SensorMonitoring sensorMonitoring = getSensorMonitoring(sensorId);
        sensorMonitoring.setEnabled(false);
        sensorMonotoringRepository.saveAndFlush(sensorMonitoring);
    }

    private SensorMonitoring getSensorMonitoring(TSID sensorId) {
        return sensorMonotoringRepository.findById(sensorId)
                .orElse(SensorMonitoring.builder()
                        .id(new SensorId(sensorId))
                        .enabled(false)
                        .lastTemperature(null)
                        .updatedAt(null)
                        .build());
    }

}
