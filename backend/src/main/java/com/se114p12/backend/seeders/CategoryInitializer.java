package com.se114p12.backend.seeders;

import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.neo4j.entities.CategoryNode;
import com.se114p12.backend.neo4j.entities.ProductNode;
import com.se114p12.backend.neo4j.repositories.ProductNeo4jRepository;
import com.se114p12.backend.repositories.product.ProductCategoryRepository;
import com.se114p12.backend.util.ImageLoader;
import com.se114p12.backend.util.JsonLoader;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryInitializer {
  private final ImageLoader imageLoader;
  private final JsonLoader jsonLoader;
  private final ProductCategoryRepository productCategoryRepository;
  private final ProductNeo4jRepository productNeo4jRepository;

  @Transactional
  public void initializeCategories() {
    List<ProductCategory> categories = jsonLoader.loadCategoriesFromJson();
    System.out.println("Loaded categories: " + categories.size());
    System.out.println("-- Initializing categories --");
    for (ProductCategory category : categories) {
      System.out.println("Processing category: " + category.getName());
      for (Product product : category.getProducts()) {
        product.getVariations().stream()
            .forEach(v -> v.getVariationOptions().forEach(o -> o.setVariation(v)));
        String path =
            imageLoader.saveImageFromUrl(product.getImageUrl(), AppConstant.PRODUCT_FOLDER);
        product.setImageUrl(path);
        product.setIsAvailable(true);
        product.setCategory(category);
      }
      String path =
          imageLoader.saveImageFromUrl(category.getImageUrl(), AppConstant.CATEGORY_FOLDER);
      category.setImageUrl(path);
    }
    List<ProductCategory> productCategories = productCategoryRepository.saveAll(categories);
    System.out.println("-- Categories initialized successfully --");
    initializeCategoryNodes(productCategories);
  }

  @Transactional
  private void initializeCategoryNodes(List<ProductCategory> categories) {
    System.out.println("-- Initialize NEO4J --");
    List<ProductNode> productNodes = new ArrayList<>();
    for (ProductCategory category : categories) {
      CategoryNode node = new CategoryNode();
      node.setId(category.getId());
      for (Product product : category.getProducts()) {
        ProductNode productNode = new ProductNode();
        productNode.setId(product.getId());
        productNode.setCategory(node);
        productNodes.add(productNode);
      }
    }
    productNeo4jRepository.saveAll(productNodes);
  }
}
