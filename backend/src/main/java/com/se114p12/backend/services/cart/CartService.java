package com.se114p12.backend.services.cart;

import com.se114p12.backend.dtos.cart.CartResponseDTO;
import com.se114p12.backend.entities.cart.Cart;

import java.util.Optional;

public interface CartService {
    CartResponseDTO getCartResponseByUserId(Long userId);

    Cart getCartById(Long id);

    Cart create(Cart cart);

    void delete(Long id);

    boolean existsByIdAndUserId(Long id, Long userId);

    int countCartItemsByUserId(Long userId);
}