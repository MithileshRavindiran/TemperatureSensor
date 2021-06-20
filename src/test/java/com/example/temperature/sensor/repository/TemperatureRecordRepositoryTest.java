package com.example.temperature.sensor.repository;


import com.example.temperature.sensor.domain.entity.TemperatureRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TemperatureRecordRepositoryTest {

    @Autowired
    private TemperatureRecordRepository temperatureRecordRepository;


    /**
     * Test JpaRepositories methods which used in {@link TemperatureRecordRepository}
     */
    @Test
    public void testArticleRepository() {
        List<TemperatureRecord>   records = new ArrayList<>();
        records.add(TemperatureRecord.builder().id(1).temperatureInFahrenheit(98.5).recordedTimestamp(new Date()).deviceId(1).build());
        records.add(TemperatureRecord.builder().id(2).temperatureInFahrenheit(99.5).recordedTimestamp(new Date()).deviceId(2).build());
        List<TemperatureRecord> temperatureRecordList = (List<TemperatureRecord>) temperatureRecordRepository.saveAll(records);
        assertEquals(1, temperatureRecordList.get(0).getId());
        List<TemperatureRecord> temperatureRecordList1 = (List<TemperatureRecord>) temperatureRecordRepository.findAll();
        assertEquals(98.5, temperatureRecordList1.get(0).getTemperatureInFahrenheit());
    }
}
