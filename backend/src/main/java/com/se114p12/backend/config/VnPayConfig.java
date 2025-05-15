package com.se114p12.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VnPayConfig {
    @Value("${vnpay.tmnCode:DUMMY_CODE}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret:DUMMY_SECRET}")
    private String vnp_HashSecret;

    @Value("${vnpay.payUrl:https://sandbox.vnpay.vn/paymentv2/vpcpay.html}")
    private String vnp_PayUrl;

    @Value("${vnpay.returnUrl:http://localhost:8080/api/v1/payment/return}")
    private String vnp_ReturnUrl;

    public String getVnp_TmnCode() {
        return vnp_TmnCode;
    }

    public String getVnp_HashSecret() {
        return vnp_HashSecret;
    }

    public String getVnp_PayUrl() {
        return vnp_PayUrl;
    }

    public String getVnp_ReturnUrl() {
        return vnp_ReturnUrl;
    }
}
