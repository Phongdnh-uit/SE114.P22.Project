package com.se114p12.backend.repositories.cart;

import com.se114p12.backend.entities.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {
    boolean existsByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    boolean existsByUserId(Long userId);

    Optional<Cart> findByUserId(Long currentUserId);
}
