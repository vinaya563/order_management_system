package org.example.model;

import java.math.BigDecimal;

public record OrderResponse(
        String id,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        String orderStatus,
        String paymentStatus
) {
    public OrderResponse withStatus(String newOrderStatus, String newPaymentStatus) {
        return new OrderResponse(id, productName, quantity, unitPrice, newOrderStatus, newPaymentStatus);
    }
}
