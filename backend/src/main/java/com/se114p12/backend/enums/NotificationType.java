package com.se114p12.backend.enums;

import lombok.Getter;

public enum NotificationType {
    GENERAL("Thông báo", "Bạn có một thông báo mới."),
    ORDER_PLACED("Đơn hàng đã được đặt", "Đơn hàng #%s đã được đặt thành công."),
    ORDER_RECEIVED("Đơn hàng đã được xác nhận", "Đơn hàng #%s đã được xác nhận và đang chờ xử lý."),
    ORDER_PREPARING("Đơn hàng đang được chuẩn bị", "Đơn hàng #%s đang được chuẩn bị."),
    ORDER_DELIVERING("Đơn hàng đang được giao", "Đơn hàng #%s đang trên đường giao đến bạn."),
    ORDER_DELIVERED("Đơn hàng đã hoàn tất", "Đơn hàng #%s đã được giao thành công."),
    ORDER_CANCELLED("Đơn hàng đã bị hủy", "Đơn hàng #%s đã bị hủy."),
    ORDER_PAYMENT_SUCCEEDED("Thanh toán thành công", "Đơn hàng #%s đã được thanh toán thành công."),
    ORDER_PAYMENT_FAILED("Thanh toán thất bại", "Đơn hàng #%s đã gặp lỗi khi thanh toán."),
    PROMOTION("Khuyến mãi mới", "Một chương trình khuyến mãi mới đang chờ bạn!");

    @Getter
    private final String title;

    private final String messageTemplate;

    NotificationType(String title, String messageTemplate) {
        this.title = title;
        this.messageTemplate = messageTemplate;
    }

    public String formatMessage(String orderTxnRef) {
        if (messageTemplate == null || orderTxnRef == null) return "";
        return String.format(messageTemplate, orderTxnRef);
    }
}