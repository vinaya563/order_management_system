package org.example.data;

import org.example.model.OrderRequest;

import java.math.BigDecimal;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static OrderRequest validOrder() {
        return new OrderRequest(
                "PROD-100",
                "Wireless Headphones",
                2,
                new BigDecimal("49.99")
        );
    }
}
