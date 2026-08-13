package org.example.model;

public record PaymentResponse(
        String orderId,
        String paymentStatus,
        String transactionId,
        String message
) {
}
