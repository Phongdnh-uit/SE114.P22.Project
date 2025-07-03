package com.se114p12.backend.dtos.cart;

import com.se114p12.backend.dtos.BaseResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class CartResponseDTO extends BaseResponseDTO {
    private Long userId;
    private List<CartItemResponseDTO> cartItems;
}