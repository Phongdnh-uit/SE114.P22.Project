package com.se114p12.backend.service.variation;

import com.se114p12.backend.domain.variation.Variation;
import com.se114p12.backend.domain.variation.VariationOption;
import com.se114p12.backend.exception.DataConflictException;
import com.se114p12.backend.repository.variation.VariationOptionRepository;
import com.se114p12.backend.repository.variation.VariationRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VariationOptionService {
    private final VariationOptionRepository variationOptionRepository;
    private final VariationRepository variationRepository;

    public VariationOption create(VariationOption option) {
        Long variationId = option.getVariation() != null ? option.getVariation().getId() : null;
        if (variationId == null) {
            throw new DataConflictException("Variation ID is required.");
        }

        Variation variation = variationRepository.findById(variationId)
                .orElseThrow(() -> new DataConflictException("Variation with ID " + variationId + " not found."));

        option.setVariation(variation);
        return variationOptionRepository.save(option);
    }

    public VariationOption update(Long id, @NotNull VariationOption variationOption) {
        VariationOption existingOption = variationOptionRepository.findById(id)
                .orElseThrow(() -> new DataConflictException("Variation option with ID " + id + " does not exist."));

        if (!existingOption.getValue().equals(variationOption.getValue())) {
            validateUniqueValueWithinVariation(variationOption.getValue(), variationOption.getVariation().getId());
            existingOption.setValue(variationOption.getValue());
        }

        return variationOptionRepository.save(existingOption);
    }

    public void delete(Long id) {
        VariationOption variationOption = variationOptionRepository.findById(id)
                .orElseThrow(() -> new DataConflictException("Variation option with ID " + id + " does not exist."));

        variationOptionRepository.delete(variationOption);
    }

    private void validateUniqueValueWithinVariation(String value, Long variationId) {
        if (variationOptionRepository.existsByValueAndVariationId(value, variationId)) {
            throw new DataConflictException("Value '" + value + "' already exists within variation ID " + variationId + ".");
        }
    }
}