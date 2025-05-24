package com.se114p12.backend.mapper.promotion;

import com.se114p12.backend.dto.promotion.PromotionRequestDTO;
import com.se114p12.backend.dto.promotion.PromotionResponseDTO;
import com.se114p12.backend.entities.promotion.Promotion;
import com.se114p12.backend.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PromotionMapper extends GenericMapper<Promotion, PromotionRequestDTO, PromotionResponseDTO> {

    Promotion requestToEntity(PromotionRequestDTO dto);

    PromotionResponseDTO entityToResponse(Promotion entity);

    Promotion partialUpdate(PromotionRequestDTO dto, @MappingTarget Promotion entity);
}