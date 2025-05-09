package com.se114p12.backend.services.product;

import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.product.ProductCategoryRepository;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public PageVO<ProductCategory> getAll(Pageable pageable) {
        Page<ProductCategory> productCategoryPage = productCategoryRepository.findAll(pageable);
        return PageVO.<ProductCategory>builder()
                .content(productCategoryPage.getContent())
                .page(productCategoryPage.getNumber())
                .size(productCategoryPage.getSize())
                .totalElements(productCategoryPage.getTotalElements())
                .totalPages(productCategoryPage.getTotalPages())
                .numberOfElements(productCategoryPage.getNumberOfElements())
                .build();
    }

    public ProductCategory findById(Long id) {
        return productCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product category not found")
        );
    }

    public ProductCategory create(ProductCategory productCategory) {
        productCategory.setId(null);
        if (productCategoryRepository.existsByName(productCategory.getName())) {
            throw new ResourceNotFoundException("Product category already exists");
        }
        return productCategoryRepository.save(productCategory);
    }

    public ProductCategory update(Long id, ProductCategory productCategory) {
        ProductCategory existingProductCategory = findById(id);
        if (!productCategory.getName().equals(existingProductCategory.getName()) &&
                productCategoryRepository.existsByName(productCategory.getName())) {
            throw new ResourceNotFoundException("Product category already exists");
        }
        existingProductCategory.setName(productCategory.getName());
        existingProductCategory.setDescription(productCategory.getDescription());
        return productCategoryRepository.save(existingProductCategory);
    }

    public void delete(Long id) {
        if (!productCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product category not found");
        }
        productCategoryRepository.deleteById(id);
    }

}
