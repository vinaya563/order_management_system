package org.example.tests;

import org.example.base.BaseTest;
import org.example.config.TestConfig;
import org.example.data.TestDataFactory;
import org.example.model.OrderRequest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OrderDataValidationTest extends BaseTest {

    @Test
    public void createdOrderShouldContainCorrectData() {
        OrderRequest expectedOrder = TestDataFactory.validOrder();

        Response loginResponse = apiClient.login(TestConfig.validEmail(), TestConfig.validPassword());
        Assert.assertEquals(loginResponse.statusCode(), 200);
        String token = loginResponse.jsonPath().getString("token");

        Response orderResponse = apiClient.createOrder(token, expectedOrder);
        Assert.assertEquals(orderResponse.statusCode(), 201);

        String orderId = orderResponse.jsonPath().getString("id");

        Assert.assertEquals(orderResponse.jsonPath().getString("productId"), expectedOrder.productId());
        Assert.assertEquals(orderResponse.jsonPath().getString("productName"), expectedOrder.productName());
        Assert.assertEquals(orderResponse.jsonPath().getInt("quantity"), expectedOrder.quantity());
        Assert.assertEquals(orderResponse.jsonPath().getString("orderStatus"), "CREATED");
        Assert.assertEquals(orderResponse.jsonPath().getString("paymentStatus"), "PENDING");

        databaseValidator.assertOrderMatches(orderId, expectedOrder, "CREATED");
    }
}
