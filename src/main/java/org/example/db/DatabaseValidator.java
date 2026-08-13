package org.example.db;

import org.example.model.OrderRequest;
import org.example.model.OrderResponse;

public class DatabaseValidator {

    private final OrderRepository repository;

    public DatabaseValidator(OrderRepository repository) {
        this.repository = repository;
    }

    public void assertOrderMatches(
            String orderId,
            OrderRequest expected,
            String expectedStatus
    ) {
        OrderResponse actual = repository.findById(orderId)
                .orElseThrow(() ->
                        new AssertionError("Order not found in database: " + orderId));

        check(actual.id().equals(orderId), "Order ID mismatch");
        check(actual.productId().equals(expected.productId()), "Product ID mismatch");
        check(actual.productName().equals(expected.productName()), "Product name mismatch");
        check(actual.quantity() == expected.quantity(), "Quantity mismatch");
        check(actual.unitPrice().compareTo(expected.unitPrice()) == 0, "Unit price mismatch");
        check(actual.totalAmount().compareTo(expected.totalAmount()) == 0, "Total amount mismatch");
        check(actual.orderStatus().equals(expectedStatus), "Order status mismatch");
    }

    private void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
