package com.se114p12.backend.controllers.payment;

import com.se114p12.backend.annotations.ErrorResponse;
import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.payment.VnPayRequestDTO;
import com.se114p12.backend.services.order.OrderService;
import com.se114p12.backend.services.payment.VnPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "VNPAY Payment Module", description = "Endpoints for integrating VNPAY payment")
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.API_BASE_PATH + "/payment")
public class VnPayController {

    private final OrderService orderService;
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

    @Operation(summary = "Handle VNPAY return", description = "Handle return callback from VnPay after user completes payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Result of payment process", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    @ErrorResponse
    @GetMapping("/return")
    public RedirectView handleReturn(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> {
            if (v != null && v.length > 0) params.put(k, v[0]);
        });

        String txnRef = params.get("vnp_TxnRef");
        String rspCode = params.get("vnp_ResponseCode");

        boolean valid = vnPayService.verifyPayment(params, params.get("vnp_SecureHash"));

        // Lấy redirectUrl đã lưu
        String redirectUrl = vnPayService.getRedirectUrlFor(txnRef);

        if (valid && "00".equals(rspCode)) {
            Long orderId = orderService.markPaymentCompleted(txnRef);
            redirectUrl += "?success=true&orderId=" + orderId;
        } else {
            orderService.markPaymentFailed(txnRef);
            redirectUrl += "?success=false";
        }

        return new RedirectView(redirectUrl);
    }
}
