package com.se114p12.backend.mappers.product;

import com.se114p12.backend.dtos.product.ProductRequestDTO;
import com.se114p12.backend.dtos.product.ProductResponseDTO;
import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.mappers.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper
    extends GenericMapper<Product, ProductRequestDTO, ProductResponseDTO> {

  @Override
  @Mapping(target = "imageUrl", ignore = true) // ảnh xử lý riêng trong service
  @Mapping(target = "category", ignore = true) // xử lý riêng từ categoryId
  Product requestToEntity(ProductRequestDTO request);

  @Override
  @Mapping(target = "categoryId", source = "category.id")
  @Mapping(target = "categoryName", source = "category.name")
  ProductResponseDTO entityToResponse(Product entity);

  @Override
  @Mapping(target = "imageUrl", ignore = true)
  @Mapping(target = "category", ignore = true)
  Product partialUpdate(ProductRequestDTO request, @MappingTarget Product entity);
}
