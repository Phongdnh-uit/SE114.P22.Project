package com.se114p12.backend.services.variation;

import com.se114p12.backend.dtos.variation.VariationOptionRequestDTO;
import com.se114p12.backend.dtos.variation.VariationOptionResponseDTO;
import com.se114p12.backend.entities.variation.Variation;
import com.se114p12.backend.entities.variation.VariationOption;
import com.se114p12.backend.exceptions.DataConflictException;
import com.se114p12.backend.exceptions.ResourceNotFoundException;
import com.se114p12.backend.mappers.variation.VariationOptionMapper;
import com.se114p12.backend.repositories.variation.VariationOptionRepository;
import com.se114p12.backend.repositories.variation.VariationRepository;
import com.se114p12.backend.vo.PageVO;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VariationOptionServiceImpl implements VariationOptionService {

  private final VariationOptionRepository variationOptionRepository;
  private final VariationRepository variationRepository;
  private final VariationOptionMapper variationOptionMapper;

  @Override
  public PageVO<VariationOptionResponseDTO> getByVariationId(Long variationId, Pageable pageable) {
    var page = variationOptionRepository.findByVariationId(variationId, pageable);
    return PageVO.<VariationOptionResponseDTO>builder()
        .page(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .numberOfElements(page.getNumberOfElements())
        .content(page.map(variationOptionMapper::toDto).getContent())
        .build();
  }

  @Override
  public VariationOptionResponseDTO create(VariationOptionRequestDTO dto) {
    Variation variation =
        variationRepository
            .findById(dto.getVariationId())
            .orElseThrow(() -> new ResourceNotFoundException("Variation not found"));

    if (variationOptionRepository.existsByValueAndVariationId(
        dto.getValue(), dto.getVariationId())) {
      Map<String, String> errorDetails = new HashMap<>();
      errorDetails.put("value", "Duplicate option value within the same variation.");
    }

    VariationOption entity = variationOptionMapper.toEntity(dto);
    entity.setVariation(variation);

    return variationOptionMapper.toDto(variationOptionRepository.save(entity));
  }

  @Override
  public VariationOptionResponseDTO update(Long id, VariationOptionRequestDTO dto) {
    VariationOption existing =
        variationOptionRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Variation option not found"));

    if (!existing.getValue().equals(dto.getValue())
        && variationOptionRepository.existsByValueAndVariationId(
            dto.getValue(), dto.getVariationId())) {
      Map<String, String> errorDetails = new HashMap<>();
      errorDetails.put("value", "Duplicate option value within the same variation.");
      throw new DataConflictException(errorDetails);
    }

    existing.setValue(dto.getValue());
    existing.setAdditionalPrice(dto.getAdditionalPrice());

    Variation variation =
        variationRepository
            .findById(dto.getVariationId())
            .orElseThrow(() -> new ResourceNotFoundException("Variation not found"));
    existing.setVariation(variation);

    existing
        .getCartItems()
        .forEach(
            cartItem -> {
              cartItem.setAvailable(false);
            });

    return variationOptionMapper.toDto(variationOptionRepository.save(existing));
  }

  @Override
  public void delete(Long id) {
    VariationOption option =
        variationOptionRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Variation option not found"));
    option.getCartItems().forEach(cartItem -> cartItem.setAvailable(false));
    option = variationOptionRepository.save(option);
    variationOptionRepository.delete(option);
  }
}
