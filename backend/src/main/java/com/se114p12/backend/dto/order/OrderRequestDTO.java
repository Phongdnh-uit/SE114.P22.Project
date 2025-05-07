package com.se114p12.backend.dto.order;

import com.se114p12.backend.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestDTO {
    @NotNull
    private String shippingAddress;

    private String note;

    @NotNull
    private PaymentMethod paymentMethod;
}
