package com.se114p12.backend.controllers.cart;

import com.se114p12.backend.annotations.ErrorResponse;
import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.cart.CartResponseDTO;
import com.se114p12.backend.entities.cart.Cart;
import com.se114p12.backend.mappers.cart.CartMapper;
import com.se114p12.backend.repositories.cart.CartRepository;
import com.se114p12.backend.services.cart.CartService;
import com.se114p12.backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart Module", description = "APIs for managing user carts")
@RequestMapping(AppConstant.API_BASE_PATH + "/carts")
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final CartMapper cartMapper;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "Get current user's cart",
            description = "Retrieve the cart that belongs to the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cart", content = @Content(schema = @Schema(implementation = Cart.class))),
            @ApiResponse(responseCode = "404", description = "Cart not found for current user")
    })
    @ErrorResponse
    @GetMapping("/me")
    public ResponseEntity<CartResponseDTO> getMyCart() {
        Long userId = jwtUtil.getCurrentUserId();
        CartResponseDTO cartResponse = cartService.getCartResponseByUserId(userId);
        return ResponseEntity.ok(cartResponse);
    }

    @Operation(
            summary = "Get cart by ID",
            description = "Retrieve a cart by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cart", content = @Content(schema = @Schema(implementation = Cart.class))),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @ErrorResponse
    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDTO> getCartById(@PathVariable("id") Long id) {
        if (!cartRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        CartResponseDTO cartResponse = cartMapper.toCartResponseDTO(cartService.getCartById(id));
        return ResponseEntity.ok(cartResponse);
    }

    @Operation(
            summary = "Create a new cart",
            description = "Create a new cart for the current user"
    )
    @ApiResponse(responseCode = "200", description = "Successfully created cart", content = @Content(schema = @Schema(implementation = Cart.class)))
    @ErrorResponse
    @PostMapping
    public ResponseEntity<Cart> createNewCart(
            @Parameter(description = "Cart object to be created")
            @Valid @RequestBody Cart cart) {
        return ResponseEntity.ok().body(cartService.create(cart));
    }

    @Operation(
            summary = "Delete a cart by ID",
            description = "Delete a cart belonging to the current user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted cart"),
            @ApiResponse(responseCode = "404", description = "Cart not found or does not belong to current user")
    })
    @ErrorResponse
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(
            @Parameter(description = "ID of the cart to be deleted")
            @Valid @PathVariable("id") Long id) {
        if (!cartService.existsByIdAndUserId(id, jwtUtil.getCurrentUserId())) {
            return ResponseEntity.notFound().build();
        }
        cartService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Count cart items of current user", description = "Get the number of items in current user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully counted items", content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @ErrorResponse
    @GetMapping("/me/count")
    public ResponseEntity<Integer> countCartItemsOfCurrentUser() {
        Long userId = jwtUtil.getCurrentUserId();
        int itemCount = cartService.countCartItemsByUserId(userId);
        return ResponseEntity.ok(itemCount);
    }
}