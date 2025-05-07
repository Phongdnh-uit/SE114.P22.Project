package com.se114p12.backend.mapper;

import com.se114p12.backend.domains.shipper.Shipper;
import com.se114p12.backend.dto.request.shipper.ShipperRequest;
import com.se114p12.backend.dto.response.shipper.ShipperResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShipperMapper {

    Shipper toEntity(ShipperRequest request);

    ShipperResponse toResponse(Shipper entity);

    List<ShipperResponse> toResponseList(List<Shipper> shippers);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Shipper shipper, ShipperRequest request);
}