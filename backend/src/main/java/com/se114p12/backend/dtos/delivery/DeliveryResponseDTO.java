package com.se114p12.backend.dtos.delivery;

import lombok.Data;

@Data
public class DeliveryResponseDTO {
    private int expectedDeliveryTimeInSeconds;
    private String durationText;
    private String estimatedDeliveryTime;

    public DeliveryResponseDTO(int durationInSeconds, String durationText, String estimatedDeliveryTime) {
        this.expectedDeliveryTimeInSeconds = durationInSeconds;
        this.durationText = durationText;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }
}
