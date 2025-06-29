package com.se114p12.backend.services.cart;

import com.se114p12.backend.dtos.cart.CartItemRequestDTO;
import com.se114p12.backend.dtos.cart.CartItemResponseDTO;
import com.se114p12.backend.entities.cart.Cart;
import com.se114p12.backend.entities.cart.CartItem;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.exceptions.DataConflictException;
import com.se114p12.backend.mappers.cart.CartItemMapper;
import com.se114p12.backend.repositories.authentication.UserRepository;
import com.se114p12.backend.repositories.cart.CartItemRepository;
import com.se114p12.backend.repositories.cart.CartRepository;
import com.se114p12.backend.repositories.product.ProductRepository;
import com.se114p12.backend.repositories.variation.VariationOptionRepository;
import com.se114p12.backend.util.JwtUtil;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final VariationOptionRepository variationOptionRepository;

    @Override
    public PageVO<CartItemResponseDTO> getAllCartItems(Pageable pageable) {
        Page<CartItem> page = cartItemRepository.findAll(pageable);
        return PageVO.<CartItemResponseDTO>builder()
                .content(page.map(cartItemMapper::toDTO).getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .numberOfElements(page.getNumberOfElements())
                .build();
    }

    @Override
    public CartItemResponseDTO createCartItem(CartItemRequestDTO dto) {
        Long userId = jwtUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataConflictException("User not found"));

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartService.create(cart);
        }

        CartItem newItem = cartItemMapper.toEntity(dto, productRepository, variationOptionRepository);
        newItem.setCart(cart);

        BigDecimal basePrice = newItem.getProduct().getOriginalPrice();
        BigDecimal additionalPrice = newItem.getVariationOptions().stream()
                .map(vo -> BigDecimal.valueOf(vo.getAdditionalPrice() != null ? vo.getAdditionalPrice() : 0.0))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        newItem.setPrice(basePrice.add(additionalPrice));

        List<CartItem> existingItems = cartItemRepository.findAllByCartIdAndProductId(
                cart.getId(), newItem.getProduct().getId());

        for (CartItem item : existingItems) {
            if (sameVariations(item, newItem)) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return cartItemMapper.toDTO(cartItemRepository.save(item));
            }
        }

        return cartItemMapper.toDTO(cartItemRepository.save(newItem));
    }

    @Override
    public CartItemResponseDTO updateCartItem(Long id, CartItemRequestDTO dto) {
        CartItem existing = cartItemRepository.findById(id)
                .orElseThrow(() -> new DataConflictException("Cart item not found"));

        CartItem updated = cartItemMapper.toEntity(dto, productRepository, variationOptionRepository);
        updated.setId(id);
        updated.setCart(existing.getCart());

        BigDecimal basePrice = updated.getProduct().getOriginalPrice();
        BigDecimal additionalPrice = updated.getVariationOptions().stream()
                .map(vo -> BigDecimal.valueOf(vo.getAdditionalPrice() != null ? vo.getAdditionalPrice() : 0.0))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        updated.setPrice(basePrice.add(additionalPrice));

        return cartItemMapper.toDTO(cartItemRepository.save(updated));
    }

    @Override
    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    private boolean sameVariations(CartItem a, CartItem b) {
        return a.getVariationOptions().size() == b.getVariationOptions().size()
                && a.getVariationOptions().containsAll(b.getVariationOptions());
    }
}