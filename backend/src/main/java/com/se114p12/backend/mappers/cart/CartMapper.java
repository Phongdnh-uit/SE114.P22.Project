package com.se114p12.backend.mappers.cart;

import com.se114p12.backend.dtos.cart.CartItemResponseDTO;
import com.se114p12.backend.dtos.cart.CartResponseDTO;
import com.se114p12.backend.entities.cart.Cart;
import com.se114p12.backend.entities.cart.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItems", expression = "java(toCartItemResponseDTOList(cart.getCartItems()))")
    CartResponseDTO toCartResponseDTO(Cart cart);

    List<CartItemResponseDTO> toCartItemResponseDTOList(List<CartItem> cartItems);
}