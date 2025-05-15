package com.se114p12.backend.services.general;

import com.se114p12.backend.dto.general.PromotionRequestDTO;
import com.se114p12.backend.dto.general.PromotionResponseDTO;
import com.se114p12.backend.entities.general.Promotion;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PromotionService {

    PageVO<PromotionResponseDTO> getAll(Specification<Promotion> spec, Pageable pageable);

    PromotionResponseDTO findById(Long id);

    PromotionResponseDTO create(PromotionRequestDTO dto);

    PromotionResponseDTO update(Long id, PromotionRequestDTO dto);

    void delete(Long id);
}