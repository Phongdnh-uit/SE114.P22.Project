package com.se114p12.backend.mapper.general;

import com.se114p12.backend.dto.general.PromotionRequestDTO;
import com.se114p12.backend.dto.general.PromotionResponseDTO;
import com.se114p12.backend.entities.general.Promotion;
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