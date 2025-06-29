package com.se114p12.backend.controllers.delivery;

import com.se114p12.backend.configs.ShopLocationConfig;
import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.delivery.DeliveryRequestDTO;
import com.se114p12.backend.dtos.delivery.DeliveryResponseDTO;
import com.se114p12.backend.services.delivery.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Delivery Module", description = "APIs for managing deliveries")
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.API_BASE_PATH + "/delivery")
public class DeliveryController {

    private final MapService mapboxService;
    private final ShopLocationConfig shopLocationConfig;

    @Operation(summary = "Get delivery estimates", description = "Get delivery estimates based on the given origin and destination coordinates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved delivery estimates"),
            @ApiResponse(responseCode = "404", description = "Delivery estimates not found")
    })
    @PostMapping("/estimate-time")
    public ResponseEntity<DeliveryResponseDTO> estimateTime(@RequestBody DeliveryRequestDTO request) {
        double originLat = shopLocationConfig.getLat();
        double originLng = shopLocationConfig.getLng();

        DeliveryResponseDTO result = mapboxService.calculateExpectedDeliveryTime(
                originLat,
                originLng,
                request.getDestinationLat(),
                request.getDestinationLng()
        );

        return ResponseEntity.ok(result);
    }
}