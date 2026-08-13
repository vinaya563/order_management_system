package org.example.model;

import java.math.BigDecimal;

public record OrderResponse(
        String id,
        String userId,
        String productId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalAmount,
        String orderStatus,
        String paymentStatus,
        String transactionId
) {
    public OrderResponse withPaymentStatus(
            String newOrderStatus,
            String newPaymentStatus,
            String newTransactionId
    ) {
        return new OrderResponse(
                id,
                userId,
                productId,
                productName,
                quantity,
                unitPrice,
                totalAmount,
                newOrderStatus,
                newPaymentStatus,
                newTransactionId
        );
    }
}
