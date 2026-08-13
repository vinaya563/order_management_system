package org.example.model;

import java.math.BigDecimal;

public record OrderRequest(
        String productName,
        int quantity,
        BigDecimal unitPrice
) {
}
