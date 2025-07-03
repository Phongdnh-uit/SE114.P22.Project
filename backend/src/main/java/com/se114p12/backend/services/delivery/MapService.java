package com.se114p12.backend.services.delivery;

import com.se114p12.backend.dtos.delivery.DeliveryResponseDTO;

public interface MapService {
    public DeliveryResponseDTO calculateExpectedDeliveryTime(double originLat, double originLng, double destLat, double destLng);
}
