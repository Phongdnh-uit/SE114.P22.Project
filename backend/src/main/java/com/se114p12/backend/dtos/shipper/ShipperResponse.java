package com.se114p12.backend.dtos.shipper;

import lombok.Data;

import java.time.Instant;

@Data
public class ShipperResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;

    private String fullname;
    private String phone;
    private String licensePlate;
}