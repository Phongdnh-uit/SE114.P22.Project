package com.se114p12.backend.mappers.variation;

import com.se114p12.backend.dtos.variation.VariationRequestDTO;
import com.se114p12.backend.dtos.variation.VariationResponseDTO;
import com.se114p12.backend.entities.variation.Variation;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VariationMapper {
    Variation toEntity(VariationRequestDTO dto);

    @Mapping(source = "product.id", target = "productId")
    VariationResponseDTO toDTO(Variation entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(VariationRequestDTO dto, @MappingTarget Variation entity);
}
