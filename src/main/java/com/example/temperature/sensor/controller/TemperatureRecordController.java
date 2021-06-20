package com.example.temperature.sensor.controller;

import com.example.temperature.sensor.domain.dto.BulkTemperatureAddRequestWrapper;
import com.example.temperature.sensor.domain.dto.Error;
import com.example.temperature.sensor.domain.dto.TemperatureAddRequest;
import com.example.temperature.sensor.domain.dto.TemperatureRecordDTO;
import com.example.temperature.sensor.service.TemperatureSensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/temperature")
@RequiredArgsConstructor
public class TemperatureRecordController {

    private final TemperatureSensorService temperatureSensorService;

    /**
     * To retrieve a list of the temperature records for a particular time period
     * @param startTime hold  the  time  in seconds from which  the record to be retrieved
     * @param endTime  hold  the  time  in seconds till which  the record to be retrieved
     * @param deviceId hold the  deviceId requesting the information
     * @return list of TemperatureRecord
     */
    @Operation(summary = "Get Temperature Record",
            description = "Device Retrieves the  temperature record of the user for daily or hourly requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved  the  latest temperature record",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TemperatureRecordDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request mandatory  fields missing", content = @Content(mediaType = "application/json",schema = @Schema(implementation = Error.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Exception", content = @Content(mediaType = "application/json",schema = @Schema(implementation = Error.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTemperatures(@RequestParam(required = false) Long startTime, @RequestParam(required = false) Long endTime, @RequestParam Integer deviceId) {
        // added deviceId For project purpose but ideally this should be  coming  from  JWT/Authorization token
        List<TemperatureRecordDTO> list = temperatureSensorService.getTemperatureRecords(startTime, endTime, deviceId);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    /**
     * Method to add a temperature record to the database
     * @param temperatureAddRequest {@link TemperatureAddRequest}
     * @return Error or an  success void response with respective http status
     */
    @Operation(summary = "Add Temperature Record",
            description = "Device updates the  latest temperature record of the user through  this endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully added  the  latest temperature record"),
            @ApiResponse(responseCode = "400", description = "Bad Request mandatory  fields missing", content = @Content(mediaType = "application/json",schema = @Schema(implementation = Error.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Exception", content = @Content(mediaType = "application/json",schema = @Schema(implementation = Error.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addTemperatureRecord(@RequestBody @Valid TemperatureAddRequest temperatureAddRequest) {
        temperatureSensorService.addTemperatureRecord(temperatureAddRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * Method to add a bulk  of temperature record to the database
     * @param bulkTemperatureAddRequestWrapper {@link BulkTemperatureAddRequestWrapper}
     * @return Error or an  success void response with respective http status
     */
    @Operation(summary = "Adding Bulk Temperature Records ",
            description = "Device updates the  list of temperature record of the user through  this endpoint when device connects to internet after a while")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully added  the  list of temperature record"),
            @ApiResponse(responseCode = "400", description = "Bad Request mandatory  fields missing", content = @Content(mediaType = "application/json",schema = @Schema(implementation = Error.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Exception", content = @Content(mediaType = "application/json",schema = @Schema(implementation = Error.class)))
    })
    @PostMapping(path = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addTemperatureRecords(@RequestBody @Valid BulkTemperatureAddRequestWrapper bulkTemperatureAddRequestWrapper) {
        temperatureSensorService.addTemperatureRecords(bulkTemperatureAddRequestWrapper.getBulkTemperatureAddRequest());
        return new ResponseEntity<>(HttpStatus.MULTI_STATUS);
    }
}
