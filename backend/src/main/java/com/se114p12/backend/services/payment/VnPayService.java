package com.se114p12.backend.services.payment;

import com.se114p12.backend.configs.VnPayConfig;
import com.se114p12.backend.dtos.payment.VnPayRequestDTO;
import com.se114p12.backend.entities.order.Order;
import com.se114p12.backend.enums.PaymentStatus;
import com.se114p12.backend.repositories.order.OrderRepository;
import com.se114p12.backend.util.VnPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class VnPayService {

    private final VnPayConfig vnPayConfig;
    private final OrderRepository orderRepository;

    private static final Map<String, String> redirectUrlMap = new ConcurrentHashMap<>();

    public Map<String, Object> createPaymentUrl(VnPayRequestDTO dto, HttpServletRequest req) {
        Order order = orderRepository.findById(dto.getOrderId()).orElse(null);

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_IpAddr = vnPayConfig.getClientIp(req);
        String vnp_TmnCode = vnPayConfig.getVnp_TmnCode();

        assert order != null;
        int amount = order.getTotalPrice().intValue() * 100;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (dto.getBankCode() != null && !dto.getBankCode().isEmpty()) {
            vnp_Params.put("vnp_BankCode", dto.getBankCode());
        }

        assert order != null;
        vnp_Params.put("vnp_TxnRef", order.getTxnRef());

        vnp_Params.put("vnp_OrderInfo", dto.getOrderInfo());
        vnp_Params.put("vnp_OrderType", dto.getOrderType());

        vnp_Params.put("vnp_Locale", dto.getLanguage() != null ? dto.getLanguage() : "vn");

        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getVnp_ReturnUrl());

        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Map các trường Billing (theo chuẩn VNPAY)
        if (dto.getBillingMobile() != null && !dto.getBillingMobile().isEmpty())
            vnp_Params.put("vnp_Bill_Mobile", dto.getBillingMobile());
        if (dto.getBillingEmail() != null && !dto.getBillingEmail().isEmpty())
            vnp_Params.put("vnp_Bill_Email", dto.getBillingEmail());
        if (dto.getBillingFullname() != null && !dto.getBillingFullname().isEmpty()) {
            // Tách FirstName và LastName (First = từ đầu đến space đầu tiên, Last = từ space cuối)
            String[] nameParts = dto.getBillingFullname().trim().split("\\s+");
            if (nameParts.length > 1) {
                vnp_Params.put("vnp_Bill_FirstName", nameParts[0]);
                StringBuilder lastName = new StringBuilder();
                for (int i = 1; i < nameParts.length; i++) {
                    lastName.append(nameParts[i]);
                    if (i < nameParts.length - 1) lastName.append(" ");
                }
                vnp_Params.put("vnp_Bill_LastName", lastName.toString());
            } else {
                vnp_Params.put("vnp_Bill_FirstName", nameParts[0]);
                vnp_Params.put("vnp_Bill_LastName", "");
            }
        }
        if (dto.getBillingAddress() != null && !dto.getBillingAddress().isEmpty())
            vnp_Params.put("vnp_Bill_Address", dto.getBillingAddress());
        if (dto.getBillingCity() != null && !dto.getBillingCity().isEmpty())
            vnp_Params.put("vnp_Bill_City", dto.getBillingCity());
        if (dto.getBillingCountry() != null && !dto.getBillingCountry().isEmpty())
            vnp_Params.put("vnp_Bill_Country", dto.getBillingCountry());
        if (dto.getBillingState() != null && !dto.getBillingState().isEmpty())
            vnp_Params.put("vnp_Bill_State", dto.getBillingState());

        // Map các trường Invoice (theo chuẩn VNPAY)
        if (dto.getInvMobile() != null && !dto.getInvMobile().isEmpty())
            vnp_Params.put("vnp_Inv_Phone", dto.getInvMobile());
        if (dto.getInvEmail() != null && !dto.getInvEmail().isEmpty())
            vnp_Params.put("vnp_Inv_Email", dto.getInvEmail());
        if (dto.getInvCustomer() != null && !dto.getInvCustomer().isEmpty())
            vnp_Params.put("vnp_Inv_Customer", dto.getInvCustomer());
        if (dto.getInvAddress() != null && !dto.getInvAddress().isEmpty())
            vnp_Params.put("vnp_Inv_Address", dto.getInvAddress());
        if (dto.getInvCompany() != null && !dto.getInvCompany().isEmpty())
            vnp_Params.put("vnp_Inv_Company", dto.getInvCompany());
        if (dto.getInvTaxCode() != null && !dto.getInvTaxCode().isEmpty())
            vnp_Params.put("vnp_Inv_Taxcode", dto.getInvTaxCode());
        if (dto.getInvType() != null && !dto.getInvType().isEmpty())
            vnp_Params.put("vnp_Inv_Type", dto.getInvType());

        // Build hashData: sắp xếp key, chỉ field value khác rỗng, KHÔNG encode!
        List<String> fieldNames = new ArrayList<>();
        for (String key : vnp_Params.keySet()) {
            String value = vnp_Params.get(key);
            if (value != null && !value.isEmpty()) {
                fieldNames.add(key);
            }
        }
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName  = fieldNames.get(i);
            String fieldValue = vnp_Params.get(fieldName);

            String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8);
            hashData.append(fieldName).append("=").append(encodedValue);
            query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                    .append("=").append(encodedValue);

            if (i < fieldNames.size() - 1) {
                hashData.append("&");
                query.append("&");
            }
        }

        String vnp_SecureHash = VnPayConfig.hmacSHA512(vnPayConfig.getVnp_HashSecret(), hashData.toString());
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + query.toString() + "&vnp_SecureHash=" + vnp_SecureHash;

        Map<String, Object> job = new HashMap<>();
        job.put("code", "00");
        job.put("message", "success");
        job.put("data", paymentUrl);

        // Lưu địa chỉ điều hướng về app
        redirectUrlMap.put(order.getTxnRef(), dto.getRedirectUrl());

        return job;
    }

    // Xác thực phiên thanh toán
    public boolean verifyPayment(Map<String, String> vnpParams, String vnpSecureHash) {
        Map<String, String> params = new HashMap<>(vnpParams);
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        List<String> fieldNames = params.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .sorted()
                .toList();

        StringBuilder hashData = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String key   = fieldNames.get(i);
            String value = URLEncoder.encode(params.get(key), StandardCharsets.UTF_8);
            hashData.append(key).append("=").append(value);
            if (i < fieldNames.size() - 1) hashData.append("&");
        }

        // Tính chữ ký
        String calcHash = VnPayConfig.hmacSHA512(vnPayConfig.getVnp_HashSecret(), hashData.toString());

        return calcHash.equalsIgnoreCase(vnpSecureHash);
    }

    // Trả về đường dẫn điều hướng về app
    public String getRedirectUrlFor(String txnRef) {
        return redirectUrlMap.getOrDefault(txnRef, "mam://payment/result?success=false");
    }
}