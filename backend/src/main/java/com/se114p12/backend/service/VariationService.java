package com.se114p12.backend.service;

import com.se114p12.backend.domain.Variation;
import com.se114p12.backend.repository.VariationRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;


@Service
public class VariationService {
    private final VariationRepository variationRepository;

    public VariationService(VariationRepository variationRepository) {
        this.variationRepository = variationRepository;
    }

    public Variation create(Variation variation) {
        if (variation.getId() != null && variationRepository.existsById(variation.getId())) {
            throw new IllegalArgumentException("Variation with the given ID already exists.");
        }
        return variationRepository.save(variation);
    }

    public Variation update(Long id, @NotNull Variation variation) {
        if (!variationRepository.existsById(id)) {
            throw new IllegalArgumentException("Variation with the given ID does not exist.");
        }

        variation.setName(variation.getName());
        return variationRepository.save(variation);
    }

    public void delete(Long id) {
        if (!variationRepository.existsById(id)) {
            throw new IllegalArgumentException("Variation with the given ID does not exist.");
        }
        variationRepository.deleteById(id);
    }
}