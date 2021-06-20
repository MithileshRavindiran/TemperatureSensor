package com.example.temperature.sensor.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Request Objec to Add the temperature record from the  device
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureAddRequest {

    // Ideally the deviceId should be  coming  in from the  JWT/Authorziation  token.
    @NotNull(message = "deviceId is mandatory")
    private Integer deviceId;

    @NotNull(message = "Temperature Recode is mandatory")
    private Double temperatureInFahrenheit;

    @NotNull(message = "time stamp of the record is mandatory")
    private Long recordedTimestamp;
}
