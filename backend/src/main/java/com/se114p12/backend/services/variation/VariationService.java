package com.se114p12.backend.services.variation;

import com.se114p12.backend.dtos.variation.VariationRequestDTO;
import com.se114p12.backend.dtos.variation.VariationResponseDTO;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;

public interface VariationService {
    PageVO<VariationResponseDTO> getVariationsByProductId(Long productId, String nameFilter, Pageable pageable);

    VariationResponseDTO create(VariationRequestDTO request);
    VariationResponseDTO update(Long id, VariationRequestDTO request);
    void delete(Long id);
}