package com.se114p12.backend.service;

import com.se114p12.backend.domain.Cart;
import com.se114p12.backend.exception.DataConflictException;
import com.se114p12.backend.exception.ResourceNotFoundException;
import com.se114p12.backend.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with ID: " + id));
    }

    public Cart create(Cart cart) {
        if (existsByIdAndUserId(cart.getId(), cart.getUserId())) {
            throw new DataConflictException("This cart already exists");
        }
        return cartRepository.save(cart);
    }

    public void delete(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cart does not exist");
        }
        cartRepository.deleteById(id);
    }

    public boolean existsByIdAndUserId(Long id, Long userId) {
        return cartRepository.existsByIdAndUserId(id, userId);
    }
}