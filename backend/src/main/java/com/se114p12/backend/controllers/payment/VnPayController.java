package com.se114p12.backend.controllers.payment;

import com.se114p12.backend.services.payment.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/vnpay")
public class VnPayController {
    @Autowired
    private VnPayService vnPayService;

    @GetMapping("/create-payment")
    public String createPayment(@RequestParam Long orderId, @RequestParam Double amount, HttpServletRequest request) throws Exception {
        String ipAddress = request.getRemoteAddr();
        return vnPayService.createPaymentUrl(orderId, amount, "Thanh toan don hang " + orderId, ipAddress);
    }

    @GetMapping("/return")
    public String handleReturn(@RequestParam Map<String, String> params) {
        String secureHash = params.get("vnp_SecureHash");
        boolean isValid = vnPayService.verifyPayment(params, secureHash);

        if (isValid) {
            String responseCode = params.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                // Thanh toán thành công
                return "Thanh toán thành công đơn hàng #" + params.get("vnp_TxnRef");
            } else {
                // Thanh toán thất bại
                return "Thanh toán thất bại: Mã lỗi " + responseCode;
            }
        } else {
            return "Sai chữ ký! Không xác thực được dữ liệu.";
        }
    }
}
