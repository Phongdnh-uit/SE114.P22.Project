package com.se114p12.backend.services.cart;

import com.se114p12.backend.entities.cart.CartItem;
import com.se114p12.backend.exceptions.DataConflictException;
import com.se114p12.backend.repositories.cart.CartItemRepository;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public PageVO<CartItem> getAllCartItems(Pageable pageable) {
        Page<CartItem> cartItemPage = cartItemRepository.findAll(pageable);
        return PageVO.<CartItem>builder()
                .content(cartItemPage.getContent())
                .page(cartItemPage.getNumber())
                .size(cartItemPage.getSize())
                .totalElements(cartItemPage.getTotalElements())
                .totalPages(cartItemPage.getTotalPages())
                .numberOfElements(cartItemPage.getNumberOfElements())
                .build();
    }

    public CartItem createCartItem(CartItem cartItem) {
        cartItemRepository.findById(cartItem.getId()).ifPresent(existingItem -> {
            throw new DataConflictException("The cart item already exists");
        });
        return cartItemRepository.save(cartItem);
    }

    public CartItem updateCartItem(CartItem cartItem) {
        cartItemRepository.findById(cartItem.getId()).orElseThrow(() ->
                new DataConflictException("The cart item does not exist"));
        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }
}