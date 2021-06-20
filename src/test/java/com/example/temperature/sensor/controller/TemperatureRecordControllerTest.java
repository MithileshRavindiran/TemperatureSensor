package com.example.temperature.sensor.controller;

import com.example.temperature.sensor.domain.dto.TemperatureAddRequest;
import com.example.temperature.sensor.domain.dto.TemperatureRecordDTO;
import com.example.temperature.sensor.exception.GlobalExceptionHandler;
import com.example.temperature.sensor.service.TemperatureSensorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WebMvcTest(TemperatureRecordController.class)
public class TemperatureRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TemperatureSensorService temperatureSensorServiceMock;



    ObjectMapper objectMapper;

    List<TemperatureRecordDTO> temperatureRecordList;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        temperatureRecordList = new ArrayList<>();
        long timeStamp = LocalDateTime.of(2021, 6, 19, 18, 41, 16).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()/1000;
        temperatureRecordList.add(TemperatureRecordDTO.builder().temperatureInFahrenheit(98.5).recordedTimestamp(timeStamp).build());
        temperatureRecordList.add(TemperatureRecordDTO.builder().temperatureInFahrenheit(99.5).recordedTimestamp(timeStamp).build());
    }



    @Test
    public void whenAddingASingleTemperatureRecord_thenControllerProcess_expectSuccessfulAddition() throws Exception {
        doNothing().when(temperatureSensorServiceMock).addTemperatureRecord(isA(TemperatureAddRequest.class));
        String request = "{\"deviceId\":2,\"temperatureInFahrenheit\":98.4,\"recordedTimestamp\":1623784100}";
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/temperature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
        verify(temperatureSensorServiceMock, times(1)).addTemperatureRecord(isA(TemperatureAddRequest.class));
    }

    @Test
    public void whenAddingASingleTemperatureRecordWithNullDeviceId_thenControllerProcess_expectFailureAddition() throws Exception {
        doNothing().when(temperatureSensorServiceMock).addTemperatureRecord(isA(TemperatureAddRequest.class));
        String request = "{\"deviceId\":2,\"temperatureInFahrenheit\":98.4}";;
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/temperature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        assertAll("validation",
                () -> assertNotNull(mvcResult),
                () -> assertNotNull(mvcResult.getResponse()));
    }

    @Test
    public void whenAddingASingleTemperatureRecordWithNullRecordedTimeStamp_thenControllerProcess_expectFailureAddition() throws Exception {
        doNothing().when(temperatureSensorServiceMock).addTemperatureRecord(any());
        String request = "{\"deviceId\":2,\"recordedTimestamp\":1623784100}";
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/temperature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        assertAll("validation",
                () -> assertNotNull(mvcResult),
                () -> assertNotNull(mvcResult.getResponse()));
    }

    @Test
    public void whenAddingASingleTemperatureRecordWithNullTemperatureIn_thenControllerProcess_expectFailureAddition() throws Exception {
        doNothing().when(temperatureSensorServiceMock).addTemperatureRecord(any());
        String request = "{\"temperatureInFahrenheit\":98.4,\"recordedTimestamp\":1623784100}";
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/temperature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        assertAll("validation",
                () -> assertNotNull(mvcResult),
                () -> assertNotNull(mvcResult.getResponse()));
    }


    @Test
    public void whenAddingBulkTemperatureRecord_thenControllerProcess_expectSuccessfulAddition() throws Exception {
        doNothing().when(temperatureSensorServiceMock).addTemperatureRecords(any());
        String request = "[{\"deviceId\":18,\"temperatureInFahrenheit\":98.4,\"recordedTimestamp\":1624039586},{\"deviceId\":14,\"temperatureInFahrenheit\":97.4,\"recordedTimestamp\":1623870500},{\"deviceId\":6,\"temperatureInFahrenheit\":98.5,\"recordedTimestamp\":1623956900},{\"deviceId\":9,\"temperatureInFahrenheit\":98.5,\"recordedTimestamp\":1623956900}]";
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/temperature/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isMultiStatus()).andReturn();
        assertAll("validation",
                () -> assertNotNull(mvcResult),
                () -> assertNotNull(mvcResult.getResponse()));
    }

    @Test
    public void whenAddingABulkTemperatureRecordWithNullDeviceId_thenControllerProcess_expectFailureAddition() throws Exception {
        doNothing().when(temperatureSensorServiceMock).addTemperatureRecord(any());
        String request = "[{\"temperatureInFahrenheit\":98.4,\"recordedTimestamp\":1624039586},{\"deviceId\":14,\"temperatureInFahrenheit\":97.4,\"recordedTimestamp\":1623870500},{\"deviceId\":6,\"temperatureInFahrenheit\":98.5,\"recordedTimestamp\":1623956900},{\"deviceId\":9,\"temperatureInFahrenheit\":98.5,\"recordedTimestamp\":1623956900}]";
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/temperature/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        assertAll("validation",
                () -> assertNotNull(mvcResult),
                () -> assertNotNull(mvcResult.getResponse()));
    }

    @Test
    public void whenAddingABulkTemperatureRecordWithNullRecordedTimeStamp_thenControllerProcess_expectFailureAddition() throws Exception {
        doNothing().when(this.temperatureSensorServiceMock).addTemperatureRecord(any());
        String request = "[{\"deviceId\":18,\"recordedTimestamp\":1624039586},{\"deviceId\":14,\"temperatureInFahrenheit\":97.4,\"recordedTimestamp\":1623870500},{\"deviceId\":6,\"temperatureInFahrenheit\":98.5,\"recordedTimestamp\":1623956900},{\"deviceId\":9,\"temperatureInFahrenheit\":98.5,\"recordedTimestamp\":1623956900}]";
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/temperature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        assertAll("validation",
                () -> assertNotNull(mvcResult),
                () -> assertNotNull(mvcResult.getResponse()));
    }

    @Test
    public void whenAddingABulkTemperatureRecordWithNullTemperatureIn_thenControllerProcess_expectFailureAddition() throws Exception {
        doNothing().when(temperatureSensorServiceMock).addTemperatureRecord(any());
        String request = "[{\"deviceId\":18,\"temperatureInFahrenheit\":98.4},{\"deviceId\":14,\"temperatureInFahrenheit\":97.4,\"recordedTimestamp\":1623870500},{\"deviceId\":6,\"temperatureInFahrenheit\":98.5,\"recordedTimestamp\":1623956900},{\"deviceId\":9,\"temperatureInFahrenheit\":98.5,\"recordedTimestamp\":1623956900}]";
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/temperature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        assertAll("validation",
                () -> assertNotNull(mvcResult),
                () -> assertNotNull(mvcResult.getResponse()));
    }


    @Test
    public void whenAddingABulkTemperatureRecordWithEmptyList_thenControllerProcess_expectFailureAddition() throws Exception {
        doNothing().when(temperatureSensorServiceMock).addTemperatureRecord(any());
        String request = "[]";
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/temperature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        assertAll("validation",
                () -> assertNotNull(mvcResult),
                () -> assertNotNull(mvcResult.getResponse()));

    }

    @Test
    public void whenRetrievingTemperatureRecord_thenControllerProcess_expectSuccessFullResponse() throws Exception {
        temperatureRecordList.size();
        when(temperatureSensorServiceMock.getTemperatureRecords(1624039586l, 1624039586l, 2)).thenReturn(temperatureRecordList);
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/temperature?startTime=1624039586&endTime=1624039586&deviceId=2")
                        .contentType(MediaType.APPLICATION_JSON)
                         )
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        verify(temperatureSensorServiceMock, times(1)).getTemperatureRecords(1624039586l, 1624039586l, 2);
        assertAll("validation",
                () -> assertNotNull(mvcResult),
                () -> assertNotNull(mvcResult.getResponse()),
                () -> assertEquals(objectMapper.writeValueAsString(temperatureRecordList), mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void whenRetrievingTemperatureRecordWithStartAndEndDateNull_thenControllerProcess_expectSuccessFullResponse() throws Exception {
        temperatureRecordList.size();
        when(temperatureSensorServiceMock.getTemperatureRecords(any(), any(), anyInt())).thenReturn(temperatureRecordList);
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/temperature?deviceId=2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        verify(temperatureSensorServiceMock, times(1)).getTemperatureRecords(any(), any(), anyInt());
        assertAll("validation",
                () -> assertNotNull(mvcResult),
                () -> assertNotNull(mvcResult.getResponse()),
                () -> assertEquals(objectMapper.writeValueAsString(temperatureRecordList), mvcResult.getResponse().getContentAsString()));
    }


    @Test
    public void whenRetrievingTemperatureRecordWithStartDateNull_thenControllerProcess_expectSuccessFullResponse() throws Exception {
        temperatureRecordList.size();
        when(temperatureSensorServiceMock.getTemperatureRecords(any(), any(), anyInt())).thenReturn(temperatureRecordList);
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/temperature?endTime=1624039586&deviceId=2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        verify(temperatureSensorServiceMock, times(1)).getTemperatureRecords(any(), any(), anyInt());
        assertAll("validation",
                () -> assertNotNull(mvcResult),
                () -> assertNotNull(mvcResult.getResponse()),
                () -> assertEquals(objectMapper.writeValueAsString(temperatureRecordList), mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void whenRetrievingTemperatureRecordWithEndDateNull_thenControllerProcess_expectSuccessFullResponse() throws Exception {
        temperatureRecordList.size();
        when(temperatureSensorServiceMock.getTemperatureRecords(any(), any(), anyInt())).thenReturn(temperatureRecordList);
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/temperature?startDate=1624039586&deviceId=2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        verify(temperatureSensorServiceMock, times(1)).getTemperatureRecords(any(), any(), anyInt());
        assertAll("validation",
                () -> assertNotNull(mvcResult),
                () -> assertNotNull(mvcResult.getResponse()),
                () -> assertEquals(objectMapper.writeValueAsString(temperatureRecordList), mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void whenRetrievingTemperatureRecordWithNoDeviceId_thenControllerProcess_expectBadRequest() throws Exception {
        temperatureRecordList.size();
        when(temperatureSensorServiceMock.getTemperatureRecords(any(), any(), anyInt())).thenReturn(temperatureRecordList);
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/temperature?startDate=1624039586")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        verify(temperatureSensorServiceMock, never()).getTemperatureRecords(any(), any(), anyInt());
        assertAll("validation",
                () -> assertNotNull(mvcResult),
                () -> assertNotNull(mvcResult.getResponse()));
    }




    private ExceptionHandlerExceptionResolver createExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            @Override
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(
                    HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(
                        GlobalExceptionHandler.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(
                        new GlobalExceptionHandler(), method);
            }
        };
        exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }
}
