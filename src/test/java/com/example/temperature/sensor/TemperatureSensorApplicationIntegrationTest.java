package com.example.temperature.sensor;

import com.example.temperature.sensor.domain.dto.BulkTemperatureAddRequestWrapper;
import com.example.temperature.sensor.domain.dto.TemperatureAddRequest;
import com.example.temperature.sensor.domain.dto.TemperatureRecordDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TemperatureSensorApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TemperatureSensorApplicationIntegrationTest {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    List<TemperatureAddRequest> temperatureAddRequests;

    @BeforeEach
    public void setup() {
        temperatureAddRequests = new ArrayList<>();
        temperatureAddRequests.add(getTemperatureAddRequest(1, 98.5, new Date().getTime()));
        temperatureAddRequests.add(getTemperatureAddRequest(2, 98.5, new Date().getTime()));
    }

    @Test
    public void testAddTemperatureRecord() {

        HttpEntity<TemperatureAddRequest> entity = new HttpEntity<>(temperatureAddRequests.get(0), new HttpHeaders());
        ResponseEntity<Object> responseEntity = restTemplate.exchange(createURLWithPort("/api/v1/temperature"), HttpMethod.POST, entity , Object.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void testBulkAddTemperatureRecords() {
        BulkTemperatureAddRequestWrapper wrapper = new BulkTemperatureAddRequestWrapper();
        wrapper.setBulkTemperatureAddRequest(temperatureAddRequests);
        HttpEntity<BulkTemperatureAddRequestWrapper> entity = new HttpEntity<>(wrapper, new HttpHeaders());
        ResponseEntity<Object> responseEntity = restTemplate.exchange(createURLWithPort("/api/v1/temperature/bulk"), HttpMethod.POST, entity, Object.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.MULTI_STATUS);
    }

    @Test
    public void testRetrieveTemperatureRecords() {
        ResponseEntity<Object> responseEntity = restTemplate.exchange(createURLWithPort("/api/v1/temperature?startTime=1624039586&endTime=1624039586&deviceId=2"), HttpMethod.GET, null, Object.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(0, ((List<TemperatureRecordDTO>)responseEntity.getBody()).size());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }


    private TemperatureAddRequest getTemperatureAddRequest(Integer deviceId, Double temperature,Long timestamp) {
        return TemperatureAddRequest.builder()
                .deviceId(deviceId).temperatureInFahrenheit(temperature).recordedTimestamp(timestamp).build();
    }
}
