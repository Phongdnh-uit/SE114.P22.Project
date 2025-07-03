package com.se114p12.backend.dtos.shipper;

import com.se114p12.backend.dtos.BaseResponseDTO;
import lombok.Data;

import java.time.Instant;

@Data
public class ShipperResponse extends BaseResponseDTO {
    private String fullname;
    private String phone;
    private String licensePlate;
    private Boolean isAvailable;
}