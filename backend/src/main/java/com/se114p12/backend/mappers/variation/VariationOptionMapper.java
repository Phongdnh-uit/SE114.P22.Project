package com.se114p12.backend.mappers.variation;

import com.se114p12.backend.dtos.variation.VariationOptionRequestDTO;
import com.se114p12.backend.dtos.variation.VariationOptionResponseDTO;
import com.se114p12.backend.entities.variation.VariationOption;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VariationOptionMapper {

    @Mapping(source = "variation.id", target = "variationId")
    VariationOptionResponseDTO toDto(VariationOption entity);

    @Mapping(target = "variation", ignore = true)
    VariationOption toEntity(VariationOptionRequestDTO dto);
}
