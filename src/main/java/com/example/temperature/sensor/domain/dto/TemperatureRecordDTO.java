package com.example.temperature.sensor.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response Object provide the temperature record back to  device
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemperatureRecordDTO {

    private Double temperatureInFahrenheit;

    private Long recordedTimestamp;
}
