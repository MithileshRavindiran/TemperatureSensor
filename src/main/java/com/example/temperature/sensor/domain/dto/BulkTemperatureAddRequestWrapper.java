package com.example.temperature.sensor.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Wrapper class to bulk update on temperature records
 */
public class BulkTemperatureAddRequestWrapper {

    @NotEmpty(message = "Temperature Record's can't be Empty")
    @Valid
    private List<TemperatureAddRequest> temperatureAddRequestList;

    @JsonCreator
    public BulkTemperatureAddRequestWrapper(List<TemperatureAddRequest> temperatureAddRequestList) {
        this.temperatureAddRequestList = temperatureAddRequestList;
    }

    public BulkTemperatureAddRequestWrapper() {
    }

    @JsonValue
    public List<TemperatureAddRequest> getBulkTemperatureAddRequest() {
        return temperatureAddRequestList;
    }

    public void setBulkTemperatureAddRequest(List<TemperatureAddRequest> temperatureAddRequestList) {
        this.temperatureAddRequestList = temperatureAddRequestList;
    }
}
