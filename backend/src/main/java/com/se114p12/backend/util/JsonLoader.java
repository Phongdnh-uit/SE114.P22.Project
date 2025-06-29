package com.se114p12.backend.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se114p12.backend.entities.product.ProductCategory;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JsonLoader {

  private final ObjectMapper objectMapper;

  /**
   * Load a JSON file from the classpath and convert it to an object of the specified type.
   *
   * @param filePath the path to the JSON file
   * @param valueType the class of the type to convert to
   * @param <T> the type of the object to return
   * @return an object of type T
   */
  public List<ProductCategory> loadCategoriesFromJson() {
    try {

      Path path = Paths.get("src/main/resources/data/mock_data.json");
      System.out.println("Loading categories from: " + path.toAbsolutePath());
      return objectMapper.readValue(path.toFile(), new TypeReference<List<ProductCategory>>() {});
    } catch (IOException e) {
      throw new RuntimeException("Failed to load categories from JSON", e);
    }
  }
}
