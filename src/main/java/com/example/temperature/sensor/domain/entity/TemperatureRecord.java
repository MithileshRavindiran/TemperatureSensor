package com.example.temperature.sensor.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name ="temperature_record", indexes = {
        @Index(name = "fn_index", columnList = "recorded_timestamp")
},uniqueConstraints=
@UniqueConstraint(columnNames={"device_id", "recorded_timestamp"}))
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TemperatureRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "device_id")
    private int deviceId;

    @Column(name ="temp_fahrenheit")
    private double temperatureInFahrenheit;

    @Column(name ="recorded_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordedTimestamp;
}
