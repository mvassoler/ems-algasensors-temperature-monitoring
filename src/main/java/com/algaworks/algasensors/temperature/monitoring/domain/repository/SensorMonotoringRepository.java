package com.algaworks.algasensors.temperature.monitoring.domain.repository;

import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import io.hypersistence.tsid.TSID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorMonotoringRepository extends JpaRepository<SensorMonitoring, TSID> {

}
