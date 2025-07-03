package com.se114p12.backend.mappers.promotion;

import com.se114p12.backend.dtos.promotion.PromotionRequestDTO;
import com.se114p12.backend.dtos.promotion.PromotionResponseDTO;
import com.se114p12.backend.entities.promotion.Promotion;
import com.se114p12.backend.mappers.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PromotionMapper extends GenericMapper<Promotion, PromotionRequestDTO, PromotionResponseDTO> {

    @Override
    @Mapping(source = "isPublic", target = "isPublic")
    PromotionResponseDTO entityToResponse(Promotion entity);

    @Override
    @Mapping(source = "isPublic", target = "isPublic")
    Promotion requestToEntity(PromotionRequestDTO dto);

    @Override
    @Mapping(source = "isPublic", target = "isPublic")
    Promotion partialUpdate(PromotionRequestDTO dto, @MappingTarget Promotion entity);
}