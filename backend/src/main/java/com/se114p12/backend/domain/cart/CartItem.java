package com.se114p12.backend.domain.cart;

import com.se114p12.backend.domain.product.ProductItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_item_id", nullable = false)
    private ProductItem productItem;

    @NotBlank
    private Long quantity;
}