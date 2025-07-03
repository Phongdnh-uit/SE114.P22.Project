package com.se114p12.backend.services.shipper;

import com.se114p12.backend.dtos.shipper.ShipperRequest;
import com.se114p12.backend.dtos.shipper.ShipperResponse;
import com.se114p12.backend.entities.shipper.Shipper;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ShipperService {
    ShipperResponse create(ShipperRequest request);
    PageVO<ShipperResponse> getAll(Specification<Shipper> specification, Pageable pageable);
    ShipperResponse getById(Long id);
    ShipperResponse update(Long id, ShipperRequest request);
    void delete(Long id);
}