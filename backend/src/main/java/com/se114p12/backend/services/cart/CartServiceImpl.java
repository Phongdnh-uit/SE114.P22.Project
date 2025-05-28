package com.se114p12.backend.services.cart;

import com.se114p12.backend.entities.cart.Cart;
import com.se114p12.backend.exceptions.DataConflictException;
import com.se114p12.backend.exceptions.ResourceNotFoundException;
import com.se114p12.backend.repositories.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with ID: " + id));
    }

    public Cart create(Cart cart) {
        if (cartRepository.existsByUserId(cart.getUser().getId())) {
            throw new DataConflictException("User already has a cart");
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