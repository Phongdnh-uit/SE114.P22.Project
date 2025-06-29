package com.se114p12.backend.controllers.payment;

import com.google.gson.JsonObject;
import com.se114p12.backend.annotations.ErrorResponse;
import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.payment.VnPayRequestDTO;
import com.se114p12.backend.services.payment.VnPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@Tag(name = "VNPAY Payment Module", description = "Endpoints for integrating VNPAY payment")
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.API_BASE_PATH + "/payment")
public class VnPayController {

    private final VnPayService vnPayService;

    @Operation(summary = "Create VnPay payment", description = "Generate a payment URL for VnPay")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment URL created successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters", content = @Content)
    })
    @ErrorResponse
    @PostMapping("/create-payment")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody VnPayRequestDTO dto, HttpServletRequest req) {
        Map<String, Object> response = vnPayService.createPaymentUrl(dto, req);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Handle IPN from VNPAY", description = "Handle Instant Payment Notification (server-to-server) from VnPay")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Result of payment process", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    @ErrorResponse
    @PostMapping("/ipn")
    public ResponseEntity<Map<String, String>> handleIpn(@RequestBody Map<String, String> params) {
        Map<String, String> result = vnPayService.handleIpn(params);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Handle VNPAY return", description = "Handle return callback from VnPay after user completes payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Result of payment process", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    @ErrorResponse
    @GetMapping("/return")
    public String handleReturn(
            @Parameter(description = "All return parameters from VnPay") @RequestParam Map<String, String> params,
            HttpServletRequest request
    ) {
        request.getParameterMap().forEach((key, value) -> {
            System.out.println("Param: " + key + "=" + Arrays.toString(value));
        });

        String secureHash = params.get("vnp_SecureHash");
        boolean isValid = vnPayService.verifyPayment(params, secureHash);

        if (isValid) {
            String responseCode = params.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                return "Thanh toán thành công đơn hàng #" + params.get("vnp_TxnRef");
            } else {
                return "Thanh toán thất bại: Mã lỗi " + responseCode;
            }
        } else {
            return "Sai chữ ký! Không xác thực được dữ liệu.";
        }
    }
}