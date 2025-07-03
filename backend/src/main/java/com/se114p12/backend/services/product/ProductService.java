package com.se114p12.backend.services.product;

import com.se114p12.backend.dtos.product.ProductRequestDTO;
import com.se114p12.backend.dtos.product.ProductResponseDTO;
import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.vo.PageVO;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ProductService {

  List<ProductResponseDTO> getRecommendedProducts();

  PageVO<ProductResponseDTO> getAllProducts(
      Specification<Product> specification, Pageable pageable);

  PageVO<ProductResponseDTO> getProductsByCategory(Long categoryId, Pageable pageable);

  ProductResponseDTO getProductById(Long id);

  ProductResponseDTO create(ProductRequestDTO dto);

  ProductResponseDTO update(Long id, ProductRequestDTO dto);

  void delete(Long id);
}
