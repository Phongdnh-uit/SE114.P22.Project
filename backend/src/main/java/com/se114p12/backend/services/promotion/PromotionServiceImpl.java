package com.se114p12.backend.services.promotion;

import com.se114p12.backend.dto.promotion.PromotionRequestDTO;
import com.se114p12.backend.dto.promotion.PromotionResponseDTO;
import com.se114p12.backend.entities.promotion.Promotion;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.mapper.promotion.PromotionMapper;
import com.se114p12.backend.repository.promotion.PromotionRepository;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    @Override
    public PageVO<PromotionResponseDTO> getAll(Specification<Promotion> spec, Pageable pageable) {
        Page<Promotion> page = promotionRepository.findAll(spec, pageable);
        List<PromotionResponseDTO> content = page.stream().map(promotionMapper::entityToResponse).toList();
        return PageVO.<PromotionResponseDTO>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .numberOfElements(page.getNumberOfElements())
                .build();
    }

    @Override
    public PromotionResponseDTO findById(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
        return promotionMapper.entityToResponse(promotion);
    }

    @Override
    public PromotionResponseDTO create(PromotionRequestDTO dto) {
        if (dto.getCode() != null && promotionRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Promotion code already exists");
        }
        Promotion promotion = promotionMapper.requestToEntity(dto);
        return promotionMapper.entityToResponse(promotionRepository.save(promotion));
    }

    @Override
    public PromotionResponseDTO update(Long id, PromotionRequestDTO dto) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
        promotionMapper.partialUpdate(dto, promotion);
        return promotionMapper.entityToResponse(promotionRepository.save(promotion));
    }

    @Override
    public void delete(Long id) {
        if (!promotionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Promotion not found");
        }
        promotionRepository.deleteById(id);
    }
}