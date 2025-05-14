package com.se114p12.backend.services.product;

import com.se114p12.backend.dto.product.ProductRequestDTO;
import com.se114p12.backend.dto.product.ProductResponseDTO;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    PageVO<ProductResponseDTO> getProductsByCategory(Long categoryId, Pageable pageable);

    ProductResponseDTO create(ProductRequestDTO dto);

    ProductResponseDTO update(Long id, ProductRequestDTO dto);

    void delete(Long id);
}