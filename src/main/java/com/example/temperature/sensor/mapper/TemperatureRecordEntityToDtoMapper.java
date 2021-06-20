package com.example.temperature.sensor.mapper;

import com.example.temperature.sensor.domain.entity.TemperatureRecord;
import com.example.temperature.sensor.domain.dto.TemperatureRecordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Date;

@Mapper
public interface TemperatureRecordEntityToDtoMapper {

    TemperatureRecordEntityToDtoMapper INSTANCE = Mappers.getMapper(TemperatureRecordEntityToDtoMapper.class);

    @Mappings({
            @Mapping(source = "recordedTimestamp", target = "recordedTimestamp", qualifiedByName = "convertTimeToLong"),
    })
    TemperatureRecordDTO entityToDto(TemperatureRecord temperatureRecord);

    @Named("convertTimeToLong")
     static Long convertTimeToLong(Date recordedTimeStamp) {
       return recordedTimeStamp.getTime()/1000;
    }
}
