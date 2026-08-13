package org.example.model;

import java.math.BigDecimal;

public record OrderRequest(
        String productId,
        String productName,
        int quantity,
        BigDecimal unitPrice
) {
    public BigDecimal totalAmount() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
