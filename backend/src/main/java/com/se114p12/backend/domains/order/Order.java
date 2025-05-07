package com.se114p12.backend.domains.order;

import com.se114p12.backend.domains.authentication.User;
import com.se114p12.backend.domains.shipper.Shipper;
import com.se114p12.backend.enums.OrderStatus;
import com.se114p12.backend.domains.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order extends BaseEntity {

  @NotNull
  private Instant orderDate;

  @NotNull
  private String shippingAddress;

  private Long totalPrice;

  private String note;

  @NotNull
  private Instant expectedDeliveryTime;

  @NotNull
  private Instant actualDeliveryTime;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

//  private Payment paymentMethod;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "shipper_id")
  private Shipper shipper;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderDetail> orderDetails;
}