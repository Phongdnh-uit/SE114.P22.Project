package com.se114p12.backend.services.promotion;

import com.se114p12.backend.dtos.promotion.PromotionRequestDTO;
import com.se114p12.backend.dtos.promotion.PromotionResponseDTO;
import com.se114p12.backend.entities.promotion.Promotion;
import com.se114p12.backend.entities.promotion.UserPromotion;
import com.se114p12.backend.entities.promotion.UserPromotionId;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.exceptions.ResourceNotFoundException;
import com.se114p12.backend.mappers.promotion.PromotionMapper;
import com.se114p12.backend.repositories.authentication.UserRepository;
import com.se114p12.backend.repositories.promotion.PromotionRepository;
import com.se114p12.backend.repositories.promotion.UserPromotionRepository;
import com.se114p12.backend.vo.PageVO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final UserRepository userRepository;
    private final PromotionRepository promotionRepository;
    private final UserPromotionRepository userPromotionRepository;
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
    @Transactional
    public PromotionResponseDTO create(PromotionRequestDTO dto) {
        if (StringUtils.hasText(dto.getCode()) && promotionRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Promotion code already exists");
        }

        Promotion promotion = promotionMapper.requestToEntity(dto);
        promotion = promotionRepository.save(promotion);

        // Nếu là promotion riêng tư và có danh sách người dùng
        if (!dto.getIsPublic() && dto.getUserIds() != null && !dto.getUserIds().isEmpty()) {
            List<User> users = userRepository.findAllById(dto.getUserIds());

            if (users.size() != dto.getUserIds().size()) {
                throw new IllegalArgumentException("One or more user IDs are invalid");
            }

            Promotion finalPromotion = promotion;
            List<UserPromotion> userPromotions = users.stream()
                    .map(user -> {
                        UserPromotion up = new UserPromotion();
                        up.setId(new UserPromotionId(user.getId(), finalPromotion.getId()));
                        up.setUser(user);
                        up.setPromotion(finalPromotion);
                        up.setUsed(false);
                        return up;
                    }).toList();

            userPromotionRepository.saveAll(userPromotions);
        }

        return promotionMapper.entityToResponse(promotion);
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