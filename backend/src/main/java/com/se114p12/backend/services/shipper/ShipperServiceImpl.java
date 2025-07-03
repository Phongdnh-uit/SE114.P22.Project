package com.se114p12.backend.services.shipper;

import com.se114p12.backend.entities.shipper.Shipper;
import com.se114p12.backend.dtos.shipper.ShipperRequest;
import com.se114p12.backend.dtos.shipper.ShipperResponse;
import com.se114p12.backend.mappers.shipper.ShipperMapper;
import com.se114p12.backend.repositories.shipper.ShipperRepository;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ShipperServiceImpl implements ShipperService {

    private final ShipperRepository shipperRepository;
    private final ShipperMapper shipperMapper;

    @Override
    public ShipperResponse create(ShipperRequest request) {
        if (shipperRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists.");
        }
        Shipper shipper = shipperMapper.toEntity(request);
        return shipperMapper.toResponse(shipperRepository.save(shipper));
    }

    @Override
    public PageVO<ShipperResponse> getAll(Specification<Shipper> specification, Pageable pageable) {
        var result = shipperRepository.findAll(specification, pageable);
        return PageVO.<ShipperResponse>builder()
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .numberOfElements(result.getNumberOfElements())
                .content(shipperMapper.toResponseList(result.getContent()))
                .build();
    }

    @Override
    public ShipperResponse getById(Long id) {
        return shipperMapper.toResponse(
                shipperRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Shipper not found"))
        );
    }

    @Override
    public ShipperResponse update(Long id, ShipperRequest request) {
        Shipper shipper = shipperRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Shipper not found"));
        shipperMapper.updateEntity(shipper, request);
        return shipperMapper.toResponse(shipperRepository.save(shipper));
    }

    @Override
    public void delete(Long id) {
        if (!shipperRepository.existsById(id)) {
            throw new NoSuchElementException("Shipper not found");
        }
        shipperRepository.deleteById(id);
    }
}