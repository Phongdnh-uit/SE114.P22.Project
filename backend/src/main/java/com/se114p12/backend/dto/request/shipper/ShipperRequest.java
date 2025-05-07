package com.se114p12.backend.dto.request.shipper;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ShipperRequest {

    @NotBlank
    private String fullname;

    @NotBlank
    private String phone;

    private String licensePlate;
}