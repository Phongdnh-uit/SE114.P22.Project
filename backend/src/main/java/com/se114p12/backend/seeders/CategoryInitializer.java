package com.se114p12.backend.seeders;

import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.repositories.product.ProductCategoryRepository;
import com.se114p12.backend.util.ImageLoader;
import com.se114p12.backend.util.JsonLoader;
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
    productCategoryRepository.saveAll(categories);
    System.out.println("-- Categories initialized successfully --");
  }
}
