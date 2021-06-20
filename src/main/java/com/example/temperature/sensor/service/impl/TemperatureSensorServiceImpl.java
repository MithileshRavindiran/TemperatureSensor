package com.example.temperature.sensor.service.impl;

import com.example.temperature.sensor.domain.dto.TemperatureAddRequest;
import com.example.temperature.sensor.domain.entity.TemperatureRecord;
import com.example.temperature.sensor.domain.dto.TemperatureRecordDTO;
import com.example.temperature.sensor.mapper.TemperatureRecordDtoToEntityMapper;
import com.example.temperature.sensor.mapper.TemperatureRecordEntityToDtoMapper;
import com.example.temperature.sensor.repository.TemperatureRecordRepository;
import com.example.temperature.sensor.service.TemperatureSensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemperatureSensorServiceImpl implements TemperatureSensorService {

    private final TemperatureRecordRepository temperatureRecordRepository;

    /**
     * Method to  add a single temperature record to database
     * @param temperatureAddRequest {@link TemperatureAddRequest}
     */
    @Override
    public void addTemperatureRecord(TemperatureAddRequest temperatureAddRequest) {
        var temperatureRecord = TemperatureRecordDtoToEntityMapper.INSTANCE.dtoToEntity(temperatureAddRequest);
        temperatureRecordRepository.save(temperatureRecord);
    }

    /***
     * Method to add bulk of  Temperature Records to the database
     * @param temperatureAddRequests Collection of {@link TemperatureAddRequest}
     */
    @Override
    public void addTemperatureRecords(List<TemperatureAddRequest> temperatureAddRequests) {
       var  temperatureRecords = temperatureAddRequests.stream().map(record ->
               TemperatureRecordDtoToEntityMapper.INSTANCE.dtoToEntity(record)).collect(Collectors.toList());
       temperatureRecordRepository.saveAll(temperatureRecords);
    }

    /**
     * To retrieve a list of the temperature records for a particular time period
     * @param startTime hold  the  time  in seconds from which  the record to be retrieved. when start date is null it will take previous date as start date
     * @param endTime  hold  the  time  in seconds till which  the record to be retrieved. when endate is null it will take the current date
     * @param deviceId hold the  deviceId requesting the information
     * @return list of TemperatureRecord {@link TemperatureRecordDTO}
     */
    @Override
    public List<TemperatureRecordDTO> getTemperatureRecords(Long startTime, Long endTime, Integer deviceId) {
        Date startDateTime = startTime != null ? new Date(startTime*1000) :  getPreviousDay(endTime);
        Date endDateTime = endTime != null ? new Date(endTime*1000) : new Date();
        List<TemperatureRecord> temperatureRecords = temperatureRecordRepository.findAllByRecordedTimestampBetweenAndDeviceId(startDateTime, endDateTime, deviceId);
        return temperatureRecords.stream().map(record -> TemperatureRecordEntityToDtoMapper.INSTANCE.entityToDto(record)).collect(Collectors.toList());
    }

    /**
     *
     * @param endTime
     * @return
     */
    private Date getPreviousDay(Long endTime) {
        Date in = endTime != null ? new Date(endTime*1000) : new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault()).minusDays(1);
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
