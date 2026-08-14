package org.example.tests;

import io.restassured.response.Response;
import org.example.db.DatabaseValidator;
import org.example.pages.OrderPage;
import org.testng.Assert;
import org.testng.annotations.Test;

// Demonstrates database validation against a dummy in-memory store, since the
// public API under test has no real database for these tests to connect to.
public class DatabaseValidationTest {

    private final OrderPage orderPage = new OrderPage();
    private final DatabaseValidator databaseValidator = new DatabaseValidator();

    @Test
    public void createdOrderIsPersistedInDummyDatabase() {
        String body = """
                {
                  "userId": 1,
                  "date": "2026-08-14",
                  "products": [{"productId": 1, "quantity": 3}]
                }
                """;

        Response response = orderPage.createOrder(body);
        int orderId = response.jsonPath().getInt("id");

        databaseValidator.recordOrder(orderId, response.getBody().asString());

        Assert.assertTrue(databaseValidator.isOrderPersisted(orderId));
        Assert.assertTrue(databaseValidator.orderDataMatches(orderId, response.getBody().asString()));
    }

    @Test
    public void unknownOrderIsNotPersistedInDummyDatabase() {
        Assert.assertFalse(databaseValidator.isOrderPersisted(99999));
    }
}
