package com.se114p12.backend.enums;

public enum NotificationType {
    GENERAL,              // Thông báo chung
    ORDER_PLACED,         // Đơn hàng đã được đặt
    ORDER_RECEIVED,       // Đơn hàng đã được tiếp nhận
    ORDER_PREPARING,      // Đơn hàng đang được chế biến
    ORDER_DELIVERING,     // Đơn hàng đang được giao
    ORDER_DELIVERED,      // Đơn hàng đã giao xong
    PROMOTION             // Thông báo khuyến mãi
}
