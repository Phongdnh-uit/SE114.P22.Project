package com.se114p12.backend.service;

import com.se114p12.backend.domain.VariationOption;
import com.se114p12.backend.exception.DataConflictException;
import com.se114p12.backend.repository.VariationOptionRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class VariationOptionService {
    private final VariationOptionRepository variationOptionRepository;

    public VariationOptionService(VariationOptionRepository variationOptionRepository) {
        this.variationOptionRepository = variationOptionRepository;
    }

    public VariationOption create(VariationOption variationOption) {
        validateUniqueValueWithinVariation(
                variationOption.getValue(),
                variationOption.getVariation().getId()
        );
        return variationOptionRepository.save(variationOption);
    }

    public VariationOption update(Long id, @NotNull VariationOption variationOption) {
        VariationOption existingOption = variationOptionRepository.findById(id)
                .orElseThrow(() -> new DataConflictException(
                        "Variation option with id " + id + " does not exist."
                ));

        if (!existingOption.getValue().equals(variationOption.getValue())) {
            validateUniqueValueWithinVariation(
                    variationOption.getValue(),
                    variationOption.getVariation().getId()
            );
            existingOption.setValue(variationOption.getValue());
        }

        return variationOptionRepository.save(existingOption);
    }

    public void delete(Long id) {
        VariationOption variationOption = variationOptionRepository.findById(id)
                .orElseThrow(() -> new DataConflictException(
                        "Variation option with id " + id + " does not exist."
                ));
        variationOptionRepository.delete(variationOption);
    }

    private void validateUniqueValueWithinVariation(String value, Long variationId) {
        if (variationOptionRepository.existsByValueAndVariationId(value, variationId)) {
            throw new DataConflictException(
                    "Value " + value + " already exists within the same variation."
            );
        }
    }
}