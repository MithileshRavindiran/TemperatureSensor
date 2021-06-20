package com.example.temperature.sensor.service;

import com.example.temperature.sensor.domain.dto.TemperatureAddRequest;
import com.example.temperature.sensor.domain.entity.TemperatureRecord;
import com.example.temperature.sensor.repository.TemperatureRecordRepository;
import com.example.temperature.sensor.service.impl.TemperatureSensorServiceImpl;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TemperatureServiceImplTest {

    @InjectMocks
    TemperatureSensorServiceImpl temperatureSensorService;

    @Mock
    TemperatureRecordRepository temperatureRecordRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenATemperatureRecordIsPushed_thenTheRecordIsAddedSuccessfully() {
        when(temperatureRecordRepository.save(any())).thenReturn(new TemperatureRecord());
        TemperatureAddRequest temperatureAddRequest = getTemperatureAddRequest(1, 98.5, 1212122323l);
        temperatureSensorService.addTemperatureRecord(temperatureAddRequest);
        verify(temperatureRecordRepository, times(1)).save(any());
    }

    @Test
    public void whenATemperatureRecordIsPushed_thenTheRecordIsAddedFailed_expectSqlException() {
        when(temperatureRecordRepository.save(any())).thenThrow(new ConstraintViolationException("violated", new SQLException("failed",  "failed"), "constraintName"));
        TemperatureAddRequest temperatureAddRequest = getTemperatureAddRequest(1, 98.5, 1212122323l);
        assertThrows(ConstraintViolationException.class, () -> temperatureSensorService.addTemperatureRecord(temperatureAddRequest));
        verify(temperatureRecordRepository, times(1)).save(any());
    }


    @Test
    public void whenAListOfTemperatureRecordsIsPushed_thenTheRecordIsAddedSuccessfully() {
        List<TemperatureRecord>   records = new ArrayList<>();
        when(temperatureRecordRepository.saveAll(any())).thenReturn(records);
        List<TemperatureAddRequest> temperatureAddRequests = new ArrayList<>();
        temperatureAddRequests.add(getTemperatureAddRequest(1, 98.5, 1212122323l));
        temperatureAddRequests.add(getTemperatureAddRequest(2, 98.5, 1212122323l));
        temperatureSensorService.addTemperatureRecords(temperatureAddRequests);
        verify(temperatureRecordRepository, times(1)).saveAll(any());
    }

    @Test
    public void whenAListOfTemperatureRecordsIsPushed_thenTheRecordAdditionFailsBecauseOfSqlException() {
        List<TemperatureRecord>   records = new ArrayList<>();
        when(temperatureRecordRepository.saveAll(any())).thenThrow(new ConstraintViolationException("violated", new SQLException("failed",  "failed"), "constraintName"));
        List<TemperatureAddRequest> temperatureAddRequests = new ArrayList<>();
        temperatureAddRequests.add(getTemperatureAddRequest(1, 98.5, 1212122323l));
        temperatureAddRequests.add(getTemperatureAddRequest(2, 98.5, 1212122323l));
        assertThrows(ConstraintViolationException.class, () -> temperatureSensorService.addTemperatureRecords(temperatureAddRequests));
        verify(temperatureRecordRepository, times(1)).saveAll(any());
    }


    @Test
    public void whenRetrievingTheTemperatureRecords_thenTheRecordIsRetrievedSuccessfully() {
        List<TemperatureRecord>   records = new ArrayList<>();
        records.add(TemperatureRecord.builder().id(1).temperatureInFahrenheit(98.5).recordedTimestamp(new Date()).deviceId(1).build());
        records.add(TemperatureRecord.builder().id(2).temperatureInFahrenheit(99.5).recordedTimestamp(new Date()).deviceId(1).build());
        when(temperatureRecordRepository.findAllByRecordedTimestampBetweenAndDeviceId(any(), any(), anyInt())).thenReturn(records);
        var tempRecordsDto = temperatureSensorService.getTemperatureRecords(1624047113l, 1624133513l, 1);
        assertAll("validating Responses",
                () -> assertNotNull(tempRecordsDto),
                () -> assertEquals(2, tempRecordsDto.size()),
                () -> assertEquals(98.5, tempRecordsDto.get(0).getTemperatureInFahrenheit()),
                () -> assertEquals(99.5, tempRecordsDto.get(1).getTemperatureInFahrenheit()));
        assertEquals(2, tempRecordsDto.size());
        verify(temperatureRecordRepository, times(1)).findAllByRecordedTimestampBetweenAndDeviceId(any(),any(), anyInt());
    }


    @Test
    public void whenRetrievingTheTemperatureRecordsWithStartAndEndDateEmpty_thenTheRecordIsRetrievedSuccessfully() {
        List<TemperatureRecord>   records = new ArrayList<>();
        records.add(TemperatureRecord.builder().id(1).temperatureInFahrenheit(98.5).recordedTimestamp(new Date()).deviceId(1).build());
        records.add(TemperatureRecord.builder().id(2).temperatureInFahrenheit(99.5).recordedTimestamp(new Date()).deviceId(1).build());
        when(temperatureRecordRepository.findAllByRecordedTimestampBetweenAndDeviceId(any(), any(), anyInt())).thenReturn(records);
        var tempRecordsDto = temperatureSensorService.getTemperatureRecords(null, null, 1);
        assertAll("validating Responses",
                () -> assertNotNull(tempRecordsDto),
                () -> assertEquals(2, tempRecordsDto.size()),
                () -> assertEquals(98.5, tempRecordsDto.get(0).getTemperatureInFahrenheit()),
                () -> assertEquals(99.5, tempRecordsDto.get(1).getTemperatureInFahrenheit()));
        assertEquals(2, tempRecordsDto.size());
        verify(temperatureRecordRepository, times(1)).findAllByRecordedTimestampBetweenAndDeviceId(any(),any(), anyInt());
    }

    @Test
    public void whenRetrievingTheTemperatureRecordsWithEndDateEmpty_thenTheRecordIsRetrievedSuccessfully() {
        List<TemperatureRecord>   records = new ArrayList<>();
        records.add(TemperatureRecord.builder().id(1).temperatureInFahrenheit(98.5).recordedTimestamp(new Date()).deviceId(1).build());
        records.add(TemperatureRecord.builder().id(2).temperatureInFahrenheit(99.5).recordedTimestamp(new Date()).deviceId(1).build());
        when(temperatureRecordRepository.findAllByRecordedTimestampBetweenAndDeviceId(any(), any(), anyInt())).thenReturn(records);
        var tempRecordsDto = temperatureSensorService.getTemperatureRecords(1624047113l, null, 1);
        assertAll("validating Responses",
                () -> assertNotNull(tempRecordsDto),
                () -> assertEquals(2, tempRecordsDto.size()),
                () -> assertEquals(98.5, tempRecordsDto.get(0).getTemperatureInFahrenheit()),
                () -> assertEquals(99.5, tempRecordsDto.get(1).getTemperatureInFahrenheit()));
        assertEquals(2, tempRecordsDto.size());
        verify(temperatureRecordRepository, times(1)).findAllByRecordedTimestampBetweenAndDeviceId(any(),any(), anyInt());
    }


    @Test
    public void whenRetrievingTheTemperatureRecordsWithStartDateEmpty_thenTheRecordIsRetrievedSuccessfully() {
        List<TemperatureRecord>   records = new ArrayList<>();
        records.add(TemperatureRecord.builder().id(1).temperatureInFahrenheit(98.5).recordedTimestamp(new Date()).deviceId(1).build());
        records.add(TemperatureRecord.builder().id(2).temperatureInFahrenheit(99.5).recordedTimestamp(new Date()).deviceId(1).build());
        when(temperatureRecordRepository.findAllByRecordedTimestampBetweenAndDeviceId(any(), any(), anyInt())).thenReturn(records);
        var tempRecordsDto = temperatureSensorService.getTemperatureRecords(null, 1624047113l, 1);
        assertAll("validating Responses",
                () -> assertNotNull(tempRecordsDto),
                () -> assertEquals(2, tempRecordsDto.size()),
                () -> assertEquals(98.5, tempRecordsDto.get(0).getTemperatureInFahrenheit()),
                () -> assertEquals(99.5, tempRecordsDto.get(1).getTemperatureInFahrenheit()));
        assertEquals(2, tempRecordsDto.size());
        verify(temperatureRecordRepository, times(1)).findAllByRecordedTimestampBetweenAndDeviceId(any(),any(), anyInt());
    }


    @Test
    public void whenRetrievingTheTemperatureRecords_thenSqlExceptionOccurs() {
        List<TemperatureRecord>   records = new ArrayList<>();
        records.add(TemperatureRecord.builder().id(1).temperatureInFahrenheit(98.5).recordedTimestamp(new Date()).deviceId(1).build());
        records.add(TemperatureRecord.builder().id(2).temperatureInFahrenheit(99.5).recordedTimestamp(new Date()).deviceId(1).build());
        when(temperatureRecordRepository.findAllByRecordedTimestampBetweenAndDeviceId(any(), any(), anyInt())).thenThrow(new NullPointerException());
        assertThrows(NullPointerException.class, () -> temperatureSensorService.getTemperatureRecords(1624047113l, 1624133513l, 1));
        verify(temperatureRecordRepository, times(1)).findAllByRecordedTimestampBetweenAndDeviceId(any(),any(), anyInt());
    }


    private TemperatureAddRequest getTemperatureAddRequest(Integer deviceId, Double temperature,Long timestamp) {
        return TemperatureAddRequest.builder()
                .deviceId(deviceId).temperatureInFahrenheit(temperature).recordedTimestamp(timestamp).build();
    }
}
