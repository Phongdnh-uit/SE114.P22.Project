package com.se114p12.backend.services.cart;

import com.se114p12.backend.dtos.cart.CartItemRequestDTO;
import com.se114p12.backend.dtos.cart.CartItemResponseDTO;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;

public interface CartItemService {
    PageVO<CartItemResponseDTO> getAllCartItems(Pageable pageable);
    CartItemResponseDTO createCartItem(CartItemRequestDTO cartItemDTO);
    CartItemResponseDTO updateCartItem(Long id, CartItemRequestDTO cartItemDTO);
    void deleteCartItem(Long id);
}