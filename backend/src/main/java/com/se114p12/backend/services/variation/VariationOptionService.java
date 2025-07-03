package com.se114p12.backend.services.variation;

import com.se114p12.backend.dtos.variation.VariationOptionRequestDTO;
import com.se114p12.backend.dtos.variation.VariationOptionResponseDTO;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;

public interface VariationOptionService {
    PageVO<VariationOptionResponseDTO> getByVariationId(Long variationId, Pageable pageable);

    VariationOptionResponseDTO create(VariationOptionRequestDTO dto);
    VariationOptionResponseDTO update(Long id, VariationOptionRequestDTO dto);
    void delete(Long id);
}