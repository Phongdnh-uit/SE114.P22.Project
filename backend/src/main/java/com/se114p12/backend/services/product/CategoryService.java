package com.se114p12.backend.services.product;

import com.se114p12.backend.dto.product.CategoryRequestDTO;
import com.se114p12.backend.dto.product.CategoryResponseDTO;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface CategoryService {
    PageVO<CategoryResponseDTO> getAll(Specification<ProductCategory> specification, Pageable pageable);

    CategoryResponseDTO findById(Long id);

    CategoryResponseDTO create(CategoryRequestDTO categoryRequestDTO);

    CategoryResponseDTO update(Long id, CategoryRequestDTO categoryRequestDTO);

    void delete(Long id);
}
