package com.se114p12.backend.repository.shipper;

import com.se114p12.backend.domains.shipper.Shipper;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipperRepository extends JpaRepository<Shipper, Long>, JpaSpecificationExecutor<Shipper> {
    boolean existsByPhone(@NotBlank String phone);
}