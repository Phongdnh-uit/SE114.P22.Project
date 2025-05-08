package com.se114p12.backend.entities.shipper;

import com.se114p12.backend.entities.BaseEntity;
import com.se114p12.backend.entities.order.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "shippers")
public class Shipper extends BaseEntity {
    @NotBlank
    private String fullname;

    @NotBlank
    @Column(unique = true)
    private String phone;

    private String licensePlate; // Biển số xe

    @OneToMany(mappedBy = "shipper")
    private List<Order> orders;
}
