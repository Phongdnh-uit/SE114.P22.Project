package com.se114p12.backend.services.product;

import com.se114p12.backend.dto.product.CategoryRequestDTO;
import com.se114p12.backend.dto.product.CategoryResponseDTO;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.mapper.product.CategoryMapper;
import com.se114p12.backend.repository.product.ProductCategoryRepository;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public PageVO<CategoryResponseDTO> getAll(Specification<ProductCategory> specification, Pageable pageable) {
        Page<ProductCategory> productCategoryPage = productCategoryRepository.findAll(specification, pageable);
        List<CategoryResponseDTO> categoryResponseDTOList = productCategoryPage.getContent()
                .stream()
                .map(categoryMapper::entityToResponse)
                .toList();
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
        ProductCategory category = productCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product category not found")
        );
        return categoryMapper.entityToResponse(category);
    }

    @Override
    public CategoryResponseDTO create(CategoryRequestDTO categoryRequestDTO) {
        if (productCategoryRepository.existsByName(categoryRequestDTO.getName())) {
            throw new ResourceNotFoundException("Product category already exists");
        }
        ProductCategory productCategory = categoryMapper.requestToEntity(categoryRequestDTO);
        productCategory = productCategoryRepository.save(productCategory);
        return categoryMapper.entityToResponse(productCategory);
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO categoryRequestDTO) {
        ProductCategory existingProductCategory = productCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product category not found")
        );
        if (!existingProductCategory.getName().equals(categoryRequestDTO.getName()) &&
                productCategoryRepository.existsByName(categoryRequestDTO.getName())) {
            throw new ResourceNotFoundException("Product category already exists");
        }
        categoryMapper.partialUpdate(categoryRequestDTO, existingProductCategory);
        existingProductCategory = productCategoryRepository.save(existingProductCategory);
        return categoryMapper.entityToResponse(existingProductCategory);
    }

    public void delete(Long id) {
        if (!productCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product category not found");
        }
        productCategoryRepository.deleteById(id);
    }

}
