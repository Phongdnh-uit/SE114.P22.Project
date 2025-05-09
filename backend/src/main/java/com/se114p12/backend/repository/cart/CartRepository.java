package com.se114p12.backend.repository.cart;

import com.se114p12.backend.entities.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {
    boolean existsByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    boolean existsByUserId(Long userId);

    Optional<Cart> findByUserId(Long currentUserId);
}
