package com.se114p12.backend.services.product;

import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.product.CategoryRequestDTO;
import com.se114p12.backend.dtos.product.CategoryResponseDTO;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.exceptions.ResourceNotFoundException;
import com.se114p12.backend.mappers.product.CategoryMapper;
import com.se114p12.backend.repositories.product.ProductCategoryRepository;
import com.se114p12.backend.services.general.StorageService;
import com.se114p12.backend.vo.PageVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

  private final ProductCategoryRepository productCategoryRepository;
  private final CategoryMapper categoryMapper;
  private final StorageService storageService;

  @Override
  public PageVO<CategoryResponseDTO> getAll(
      Specification<ProductCategory> specification, Pageable pageable) {
    Page<ProductCategory> productCategoryPage =
        productCategoryRepository.findAll(specification, pageable);
    List<CategoryResponseDTO> categoryResponseDTOList =
        productCategoryPage.getContent().stream().map(categoryMapper::entityToResponse).toList();
    return PageVO.<CategoryResponseDTO>builder()
        .content(categoryResponseDTOList)
        .page(productCategoryPage.getNumber())
        .size(productCategoryPage.getSize())
        .totalElements(productCategoryPage.getTotalElements())
        .totalPages(productCategoryPage.getTotalPages())
        .numberOfElements(productCategoryPage.getNumberOfElements())
        .build();
  }

  @Override
  public CategoryResponseDTO findById(Long id) {
    ProductCategory category =
        productCategoryRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product category not found"));
    return categoryMapper.entityToResponse(category);
  }

  @Override
  public CategoryResponseDTO create(CategoryRequestDTO categoryRequestDTO) {
    if (productCategoryRepository.existsByName(categoryRequestDTO.getName())) {
      throw new ResourceNotFoundException("Product category already exists");
    }
    ProductCategory productCategory = categoryMapper.requestToEntity(categoryRequestDTO);

    if (categoryRequestDTO.getImage() != null && !categoryRequestDTO.getImage().isEmpty()) {
      String fileUri =
          storageService.store(categoryRequestDTO.getImage(), AppConstant.CATEGORY_FOLDER);
      productCategory.setImageUrl(fileUri);
    }

    productCategory = productCategoryRepository.save(productCategory);
    return categoryMapper.entityToResponse(productCategory);
  }

  public CategoryResponseDTO update(Long id, CategoryRequestDTO categoryRequestDTO) {
    ProductCategory existingProductCategory =
        productCategoryRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product category not found"));
    if (!existingProductCategory.getName().equals(categoryRequestDTO.getName())
        && productCategoryRepository.existsByName(categoryRequestDTO.getName())) {
      throw new ResourceNotFoundException("Product category already exists");
    }
    categoryMapper.partialUpdate(categoryRequestDTO, existingProductCategory);

    if (categoryRequestDTO.getImage() != null && !categoryRequestDTO.getImage().isEmpty()) {
      String fileUri =
          storageService.store(categoryRequestDTO.getImage(), AppConstant.CATEGORY_FOLDER);
      existingProductCategory.setImageUrl(fileUri);
    }

    existingProductCategory = productCategoryRepository.save(existingProductCategory);
    return categoryMapper.entityToResponse(existingProductCategory);
  }

  @Override
  public void delete(Long id) {
    ProductCategory category =
        productCategoryRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product category not found"));

    // Nếu có hình thì xóa luôn file ảnh
    if (category.getImageUrl() != null && !category.getImageUrl().isEmpty()) {
      storageService.delete(category.getImageUrl());
    }

    productCategoryRepository.deleteById(id);
  }
}
