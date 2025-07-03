package com.se114p12.backend.mappers.product;

import com.se114p12.backend.dtos.product.CategoryRequestDTO;
import com.se114p12.backend.dtos.product.CategoryResponseDTO;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.mappers.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper extends GenericMapper<ProductCategory, CategoryRequestDTO, CategoryResponseDTO> {
    ProductCategory requestToEntity(CategoryRequestDTO dto);
    CategoryResponseDTO entityToResponse(ProductCategory entity);
    ProductCategory partialUpdate(CategoryRequestDTO dto, @MappingTarget ProductCategory entity);
}