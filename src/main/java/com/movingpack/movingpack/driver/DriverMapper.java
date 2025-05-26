package com.movingpack.movingpack.driver;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverMapper {
    DriverDto toDto(Driver driver);
    Driver toEntity(DriverDto driverDTO);
}
