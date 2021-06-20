package com.example.temperature.sensor.mapper;

import com.example.temperature.sensor.domain.entity.TemperatureRecord;
import com.example.temperature.sensor.domain.dto.TemperatureAddRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Date;

@Mapper
public interface TemperatureRecordDtoToEntityMapper {

    TemperatureRecordDtoToEntityMapper INSTANCE = Mappers.getMapper(TemperatureRecordDtoToEntityMapper.class);

    @Mappings({
            @Mapping(source = "recordedTimestamp", target = "recordedTimestamp", qualifiedByName = "convertTime"),
    })
    TemperatureRecord dtoToEntity(TemperatureAddRequest temperatureAddRequest);

    @Named("convertTime")
    static Date convertTime(long recordedTimeStamp) {
        return new Date(recordedTimeStamp*1000);
    }
}
