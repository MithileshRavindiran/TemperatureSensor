package com.example.temperature.sensor.repository;

import com.example.temperature.sensor.domain.entity.TemperatureRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Transactional(rollbackFor = SQLException.class)
public interface TemperatureRecordRepository extends CrudRepository<TemperatureRecord, Integer> {

    List<TemperatureRecord> findAllByRecordedTimestampBetweenAndDeviceId(Date startDateTime, Date endDateTime, int deviceId);
}
