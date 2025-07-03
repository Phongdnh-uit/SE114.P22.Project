package com.se114p12.backend.services.variation;

import com.se114p12.backend.dtos.variation.VariationRequestDTO;
import com.se114p12.backend.dtos.variation.VariationResponseDTO;
import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.entities.variation.Variation;
import com.se114p12.backend.exceptions.ResourceNotFoundException;
import com.se114p12.backend.mappers.variation.VariationMapper;
import com.se114p12.backend.repositories.product.ProductRepository;
import com.se114p12.backend.repositories.variation.VariationRepository;
import com.se114p12.backend.vo.PageVO;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VariationServiceImpl implements VariationService {
  private final VariationRepository variationRepository;
  private final ProductRepository productRepository;
  private final VariationMapper variationMapper;

  @Override
  public PageVO<VariationResponseDTO> getVariationsByProductId(
      Long productId, String nameFilter, Pageable pageable) {
    Specification<Variation> spec =
        (root, query, cb) -> {
          List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
          predicates.add(cb.equal(root.get("product").get("id"), productId));

          if (nameFilter != null && !nameFilter.isBlank()) {
            predicates.add(
                cb.like(cb.lower(root.get("name")), "%" + nameFilter.toLowerCase() + "%"));
          }

          return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

    Page<Variation> page = variationRepository.findAll(spec, pageable);
    Page<VariationResponseDTO> dtoPage = page.map(variationMapper::toDTO);

    return PageVO.<VariationResponseDTO>builder()
        .page(dtoPage.getNumber())
        .size(dtoPage.getSize())
        .totalElements(dtoPage.getTotalElements())
        .totalPages(dtoPage.getTotalPages())
        .numberOfElements(dtoPage.getNumberOfElements())
        .content(dtoPage.getContent())
        .build();
  }

  @Override
  public VariationResponseDTO create(VariationRequestDTO request) {
    Product product =
        productRepository
            .findById(request.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

    Variation variation = variationMapper.toEntity(request);
    variation.setProduct(product);

    return variationMapper.toDTO(variationRepository.save(variation));
  }

  @Override
  public VariationResponseDTO update(Long id, VariationRequestDTO request) {
    Variation variation =
        variationRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Variation not found."));

    if (request.getProductId() != null) {
      Product product =
          productRepository
              .findById(request.getProductId())
              .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
      variation.setProduct(product);
    }

    variationMapper.updateEntityFromDTO(request, variation);
    return variationMapper.toDTO(variationRepository.save(variation));
  }

  @Override
  public void delete(Long id) {
    if (!variationRepository.existsById(id)) {
      throw new ResourceNotFoundException("Variation not found.");
    }
    variationRepository.deleteById(id);
  }
}
