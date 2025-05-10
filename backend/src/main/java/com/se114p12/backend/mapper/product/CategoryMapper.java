package com.se114p12.backend.mapper.product;

import com.se114p12.backend.dto.product.CategoryRequestDTO;
import com.se114p12.backend.dto.product.CategoryResponseDTO;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper extends GenericMapper<ProductCategory, CategoryRequestDTO, CategoryResponseDTO> {
}
