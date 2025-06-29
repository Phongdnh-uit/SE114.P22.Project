package com.se114p12.backend.seeders;

import com.se114p12.backend.repositories.shipper.ShipperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShipperInitializer {
  private final ShipperRepository shipperRepository;

  public void initializeShippers() {}
}
