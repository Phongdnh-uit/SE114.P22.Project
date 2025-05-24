package com.se114p12.backend.services.promotion;

import com.se114p12.backend.dto.promotion.PromotionRequestDTO;
import com.se114p12.backend.dto.promotion.PromotionResponseDTO;
import com.se114p12.backend.entities.promotion.Promotion;
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