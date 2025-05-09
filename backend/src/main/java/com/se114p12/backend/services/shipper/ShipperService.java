package com.se114p12.backend.services.shipper;

import com.se114p12.backend.entities.shipper.Shipper;
import com.se114p12.backend.dto.shipper.ShipperRequest;
import com.se114p12.backend.dto.shipper.ShipperResponse;
import com.se114p12.backend.mapper.shipper.ShipperMapper;
import com.se114p12.backend.repository.shipper.ShipperRepository;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ShipperService {

    private final ShipperRepository shipperRepository;
    private final ShipperMapper shipperMapper;

    public ShipperResponse create(ShipperRequest request) {
        if (shipperRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists.");
        }
        Shipper shipper = shipperMapper.toEntity(request);
        return shipperMapper.toResponse(shipperRepository.save(shipper));
    }

    public PageVO<ShipperResponse> getAll(int page, int size) {
        var pageable = PageRequest.of(page, size);
        var result = shipperRepository.findAll(pageable);
        return PageVO.<ShipperResponse>builder()
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .numberOfElements(result.getNumberOfElements())
                .content(shipperMapper.toResponseList(result.getContent()))
                .build();
    }

    public ShipperResponse getById(Long id) {
        return shipperMapper.toResponse(
                shipperRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Shipper not found"))
        );
    }

    public ShipperResponse update(Long id, ShipperRequest request) {
        Shipper shipper = shipperRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Shipper not found"));
        shipperMapper.updateEntity(shipper, request);
        return shipperMapper.toResponse(shipperRepository.save(shipper));
    }

    public void delete(Long id) {
        if (!shipperRepository.existsById(id)) {
            throw new NoSuchElementException("Shipper not found");
        }
        shipperRepository.deleteById(id);
    }
}