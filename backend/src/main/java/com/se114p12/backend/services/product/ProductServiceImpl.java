package com.se114p12.backend.services.product;

import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.product.ProductRequestDTO;
import com.se114p12.backend.dtos.product.ProductResponseDTO;
import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.exceptions.ResourceNotFoundException;
import com.se114p12.backend.mappers.product.ProductMapper;
import com.se114p12.backend.neo4j.entities.CategoryNode;
import com.se114p12.backend.neo4j.entities.ProductNode;
import com.se114p12.backend.neo4j.repositories.ProductNeo4jRepository;
import com.se114p12.backend.neo4j.services.RecommendService;
import com.se114p12.backend.repositories.product.ProductCategoryRepository;
import com.se114p12.backend.repositories.product.ProductRepository;
import com.se114p12.backend.services.general.StorageService;
import com.se114p12.backend.vo.PageVO;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductCategoryRepository productCategoryRepository;
  private final StorageService storageService;
  private final ProductMapper productMapper;
  private final RecommendService recommendService;
  private final ProductNeo4jRepository productNeo4jRepository;

  @Override
  public PageVO<ProductResponseDTO> getAllProducts(
      Specification<Product> specification, Pageable pageable) {
    Page<Product> productPage = productRepository.findAll(specification, pageable);
    List<ProductResponseDTO> productDTOs =
        productPage.getContent().stream().map(productMapper::entityToResponse).toList();

    return PageVO.<ProductResponseDTO>builder()
        .content(productDTOs)
        .page(productPage.getNumber())
        .size(productPage.getSize())
        .totalElements(productPage.getTotalElements())
        .totalPages(productPage.getTotalPages())
        .numberOfElements(productPage.getNumberOfElements())
        .build();
  }

  @Override
  public PageVO<ProductResponseDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
    Page<Product> productPage = productRepository.findByCategory_Id(categoryId, pageable);
    List<ProductResponseDTO> productDTOs =
        productPage.map(productMapper::entityToResponse).getContent();

    return PageVO.<ProductResponseDTO>builder()
        .content(productDTOs)
        .page(productPage.getNumber())
        .size(productPage.getSize())
        .totalElements(productPage.getTotalElements())
        .totalPages(productPage.getTotalPages())
        .numberOfElements(productPage.getNumberOfElements())
        .build();
  }

  @Override
  public ProductResponseDTO create(ProductRequestDTO dto) {
    ProductCategory category =
        productCategoryRepository
            .findById(dto.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

    Product product =
        Product.builder()
            .category(category)
            .name(dto.getName())
            .shortDescription(dto.getShortDescription())
            .detailDescription(dto.getDetailDescription())
            .originalPrice(dto.getOriginalPrice())
            .isAvailable(true)
            .deleted(false)
            .build();

    if (dto.getImage() != null && !dto.getImage().isEmpty()) {
      product.setImageUrl(storageService.store(dto.getImage(), AppConstant.PRODUCT_FOLDER));
    }

    product = productRepository.save(product);

    // Neo4j recommendation system
    ProductNode productNode = new ProductNode();
    CategoryNode categoryNode = new CategoryNode();
    productNode.setId(product.getId());
    categoryNode.setId(category.getId());
    productNode.setCategory(categoryNode);
    productNeo4jRepository.save(productNode);

    return productMapper.entityToResponse(product);
  }

  @Override
  public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
    Product existingProduct =
        productRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

    existingProduct.setName(
        (dto.getName() == null || dto.getName().isEmpty())
            ? existingProduct.getName()
            : dto.getName());

    existingProduct.setShortDescription(
        (dto.getShortDescription() == null || dto.getShortDescription().isEmpty())
            ? existingProduct.getShortDescription()
            : dto.getShortDescription());

    existingProduct.setDetailDescription(
        (dto.getDetailDescription() == null || dto.getDetailDescription().isEmpty())
            ? existingProduct.getDetailDescription()
            : dto.getDetailDescription());

    existingProduct.setOriginalPrice(
        (dto.getOriginalPrice() == null)
            ? existingProduct.getOriginalPrice()
            : dto.getOriginalPrice());

    existingProduct.setUpdatedAt(Instant.now());

    if (dto.getCategoryId() != 0) {
      ProductCategory category =
          productCategoryRepository
              .findById(dto.getCategoryId())
              .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
      existingProduct.setCategory(category);
    }

    if (dto.getImage() != null && !dto.getImage().isEmpty()) {
      String fileUrl = storageService.store(dto.getImage(), AppConstant.PRODUCT_FOLDER);
      existingProduct.setImageUrl(fileUrl);
    }

    existingProduct = productRepository.save(existingProduct);

    return productMapper.entityToResponse(existingProduct);
  }

  @Override
  public void delete(Long id) {
    Optional<Product> productOptional = productRepository.findById(id);
    if (productOptional.isEmpty() || !productOptional.get().getDeleted()) {
      throw new ResourceNotFoundException("Product not found");
    }
    Product product = productOptional.get();
    product.setDeleted(true);
    product
        .getCartItems()
        .forEach(
            v -> {
              v.setAvailable(false);
            });
    productRepository.save(product);
  }

  @Override
  public ProductResponseDTO getProductById(Long id) {
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

    return productMapper.entityToResponse(product);
  }

  @Override
  public List<ProductResponseDTO> getRecommendedProducts() {
    List<Long> recommendedProductIds = recommendService.getRecommendProductIds();

    Map<Long, Product> productMap =
        productRepository.findAllById(recommendedProductIds).stream()
            .collect(Collectors.toMap(Product::getId, product -> product));

    List<Product> recommendedProducts =
        recommendedProductIds.stream()
            .map(productMap::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    if (recommendedProducts.size() < 10) {
      Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
      List<Product> additional = productRepository.findAll(pageable).getContent();
      recommendedProducts.addAll(
          additional.stream()
              .filter(product -> !recommendedProductIds.contains(product.getId()))
              .limit(10 - recommendedProducts.size())
              .toList());
    }

    return recommendedProducts.stream().map(productMapper::entityToResponse).toList();
  }
}
