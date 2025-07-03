package com.se114p12.backend.seeders;

import com.se114p12.backend.entities.shipper.Shipper;
import com.se114p12.backend.repositories.shipper.ShipperRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShipperInitializer {
  private final ShipperRepository shipperRepository;

  @Transactional
  public void initializeShippers() {
    List<Shipper> shippers = new ArrayList<>();
    shippers.add(createShipper("Nguyễn Văn A", "0123456789", "29A-12345"));
    shippers.add(createShipper("Trần Thị B", "0987654321", "30B-54321"));
    shippers.add(createShipper("Lê Văn C", "0912345678", "31C-67890"));
    shippers.add(createShipper("Phạm Thị D", "0934567890", "32D-98765"));
    shipperRepository.saveAll(shippers);
  }

  private Shipper createShipper(String fullname, String phone, String licensePlate) {
    Shipper shipper = new Shipper();
    shipper.setFullname(fullname);
    shipper.setPhone(phone);
    shipper.setLicensePlate(licensePlate);
    shipper.setIsAvailable(true);
    return shipper;
  }
}
