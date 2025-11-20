package com.algaworks.algasensors.temperature.monitoring.api.model;

import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class SensorMonitoringOutput {

    private SensorId id;
    private Double lastTemperature;
    private OffsetDateTime updatedAt;
    private Boolean enabled;
    
}
