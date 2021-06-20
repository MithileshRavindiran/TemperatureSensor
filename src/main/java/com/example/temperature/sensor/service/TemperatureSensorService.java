package com.example.temperature.sensor.service;


import com.example.temperature.sensor.domain.dto.TemperatureAddRequest;
import com.example.temperature.sensor.domain.dto.TemperatureRecordDTO;

import java.util.List;

public interface TemperatureSensorService {

    void addTemperatureRecord(TemperatureAddRequest temperatureAddRequest);

    void addTemperatureRecords(List<TemperatureAddRequest> temperatureAddRequests);


    List<TemperatureRecordDTO> getTemperatureRecords(Long startTime, Long endTime, Integer deviceId);
}
