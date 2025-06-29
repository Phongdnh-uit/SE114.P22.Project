package com.se114p12.backend.configs;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Configuration
@Data
public class VnPayConfig {
    @Value("${vnpay.tmnCode:DUMMY_CODE}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret:DUMMY_SECRET}")
    private String vnp_HashSecret;

    @Value("${vnpay.payUrl:https://sandbox.vnpay.vn/paymentv2/vpcpay.html}")
    private String vnp_PayUrl;

    @Value("${vnpay.returnUrl:http://localhost:8080/api/v1/payment/return}")
    private String vnp_ReturnUrl;

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        } else {
            ip = ip.split(",")[0]; // Nếu có nhiều IP trong chuỗi
        }
        return ip;
    }

    public static String hmacSHA512(String key, String data) {
        try {
            // Create a new Mac instance for HMAC-SHA512
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");

            // Initialize the Mac with the secret key
            mac.init(secretKeySpec);

            // Compute the HMAC
            byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Convert to hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : hmacBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage());
        }
        return "";
    }
}
