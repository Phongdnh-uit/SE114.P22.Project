package com.se114p12.backend.controllers.shipper;

import com.se114p12.backend.dto.shipper.ShipperRequest;
import com.se114p12.backend.dto.shipper.ShipperResponse;
import com.se114p12.backend.services.shipper.ShipperService;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shippers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ShipperController {

    private final ShipperService shipperService;

    @PostMapping
    public ResponseEntity<ShipperResponse> create(@RequestBody ShipperRequest request) {
        return ResponseEntity.ok(shipperService.create(request));
    }

    @GetMapping
    public ResponseEntity<PageVO<ShipperResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(shipperService.getAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipperResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(shipperService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipperResponse> update(@PathVariable Long id, @RequestBody ShipperRequest request) {
        return ResponseEntity.ok(shipperService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        shipperService.delete(id);
        return ResponseEntity.noContent().build();
    }
}