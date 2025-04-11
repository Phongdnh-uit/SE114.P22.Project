package com.se114p12.backend.controller;

import com.se114p12.backend.domain.Cart;
import com.se114p12.backend.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart Module")
@RequestMapping("/api/v1/carts")
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    //Use this method to test APIs
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable("id") Long id) {
        return cartService.existsByIdAndUserId(id, null)
                ? ResponseEntity.ok(cartService.getCartById(id))
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Cart> createNewCart(@Valid @RequestBody Cart cart) {
        return ResponseEntity.ok().body(cartService.create(cart));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@Valid @PathVariable("id") Long id) {
        if (!cartService.existsByIdAndUserId(id, null)) {
            return ResponseEntity.notFound().build();
        }
        cartService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
