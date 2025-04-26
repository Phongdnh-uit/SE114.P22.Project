package com.se114p12.backend.domains.cart;

import com.se114p12.backend.domains.BaseEntity;
import com.se114p12.backend.domains.product.Product;
import com.se114p12.backend.domains.variation.VariationOption;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    private Long quantity;

    @NotNull
    private BigDecimal price;

    @ManyToMany
    @JoinTable(
            name = "cart_item_variation_options",
            joinColumns = @JoinColumn(name = "cart_item_id"),
            inverseJoinColumns = @JoinColumn(name = "variation_option_id")
    )
    private Set<VariationOption> variationOptions;
}