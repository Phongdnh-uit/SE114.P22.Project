package com.se114p12.backend.controllers.promotion;

import com.se114p12.backend.annotations.ErrorResponse;
import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.promotion.PromotionResponseDTO;
import com.se114p12.backend.entities.promotion.Promotion;
import com.se114p12.backend.mappers.promotion.PromotionMapper;
import com.se114p12.backend.services.promotion.UserPromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "User Promotion Module", description = "Endpoints for managing user-specific promotions")
@RestController
@RequestMapping(AppConstant.API_BASE_PATH + "/user-promotions")
@RequiredArgsConstructor
public class UserPromotionController {

    private final UserPromotionService userPromotionService;
    private final PromotionMapper promotionMapper;

    @Operation(summary = "Get available promotions for a user",
            description = "Returns all unused and valid promotions for the given user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved available promotions",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PromotionResponseDTO.class))))
    })
    @ErrorResponse
    @GetMapping("/available")
    public List<PromotionResponseDTO> getAvailablePromotions(
            @Parameter(description = "User ID") @RequestParam Long userId
    ) {
        List<Promotion> promotions = userPromotionService.getAvailablePromotions(userId);
        return promotions.stream()
                .map(promotionMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get promotions applicable for an order",
            description = "Returns promotions that a user can use for a given order value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved applicable promotions",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PromotionResponseDTO.class))))
    })
    @ErrorResponse
    @GetMapping("/available-for-order")
    public List<PromotionResponseDTO> getAvailablePromotionsForOrder(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Order total value") @RequestParam BigDecimal orderValue
    ) {
        List<Promotion> promotions = userPromotionService.getAvailablePromotionsForOrder(userId, orderValue);
        return promotions.stream()
                .map(promotionMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Mark promotion as used",
            description = "Marks a promotion as used by the user (e.g., after a successful order)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promotion marked as used successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request or promotion not applicable", content = @Content)
    })
    @ErrorResponse
    @PostMapping("/use")
    public void markPromotionAsUsed(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Promotion ID") @RequestParam Long promotionId
    ) {
        userPromotionService.markPromotionAsUsed(userId, promotionId);
    }
}