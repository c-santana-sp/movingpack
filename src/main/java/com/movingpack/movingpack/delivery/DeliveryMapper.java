package com.movingpack.movingpack.delivery;

import com.movingpack.movingpack.driver.DriverMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = DriverMapper.class)
public interface DeliveryMapper {

    @Mapping(source = "driver.id", target = "driverId")
    DeliveryDto toDto(Delivery delivery);

    @Mapping(source = "driverId", target = "driver.id")
    Delivery toEntity(DeliveryDto DeliveryDto);
}
