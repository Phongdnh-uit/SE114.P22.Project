package com.se114p12.backend.services.variation;

import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.entities.variation.Variation;
import com.se114p12.backend.repository.product.ProductRepository;
import com.se114p12.backend.repository.variation.VariationRepository;
import org.springframework.stereotype.Service;


@Service
public class VariationService {
    private final VariationRepository variationRepository;
    private final ProductRepository productRepository;

    public VariationService(VariationRepository variationRepository, ProductRepository productRepository) {
        this.variationRepository = variationRepository;
        this.productRepository = productRepository;
    }

    public Variation create(Variation variation) {
        if (variation.getId() != null && variationRepository.existsById(variation.getId())) {
            throw new IllegalArgumentException("Variation with the given ID already exists.");
        }

        if (variation.getProduct() != null && variation.getProduct().getId() != null) {
            Product product = productRepository.findById(variation.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            variation.setProduct(product);
        }

        return variationRepository.save(variation);
    }

    public Variation update(Long id, Variation input) {
        Variation variation = variationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variation not found"));

        variation.setName(input.getName());

        if (input.getProduct() != null && input.getProduct().getId() != null) {
            Product product = productRepository.findById(input.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            variation.setProduct(product);
        }

        return variationRepository.save(variation);
    }

    public void delete(Long id) {
        if (!variationRepository.existsById(id)) {
            throw new IllegalArgumentException("Variation with the given ID does not exist.");
        }
        variationRepository.deleteById(id);
    }
}