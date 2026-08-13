package org.example.tests;

import io.restassured.response.Response;
import org.example.base.BaseTest;
import org.example.config.TestConfig;
import org.example.data.TestDataFactory;
import org.example.model.OrderRequest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class OrderApiTest extends BaseTest {

    @Test
    public void createdOrderContainsCorrectData() {
        OrderRequest expectedOrder = TestDataFactory.validOrder();
        String token = login();

        Response orderResponse = apiClient.createOrder(token, expectedOrder);
        Assert.assertEquals(orderResponse.statusCode(), 201);

        String orderId = orderResponse.jsonPath().getString("id");

        Assert.assertEquals(orderResponse.jsonPath().getString("productName"), expectedOrder.productName());
        Assert.assertEquals(orderResponse.jsonPath().getInt("quantity"), expectedOrder.quantity());
        Assert.assertEquals(orderResponse.jsonPath().getString("orderStatus"), "CREATED");
        Assert.assertEquals(orderResponse.jsonPath().getString("paymentStatus"), "PENDING");

        databaseValidator.assertOrderMatches(orderId, expectedOrder, "CREATED");
    }

    @Test
    public void zeroQuantityIsRejected() {
        String token = login();
        OrderRequest order = new OrderRequest("Wireless Headphones", 0, new BigDecimal("49.99"));

        Response response = apiClient.createOrder(token, order);
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    public void negativeQuantityIsRejected() {
        String token = login();
        OrderRequest order = new OrderRequest("Wireless Headphones", -1, new BigDecimal("49.99"));

        Response response = apiClient.createOrder(token, order);
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    public void blankProductNameIsRejected() {
        String token = login();
        OrderRequest order = new OrderRequest("", 1, new BigDecimal("49.99"));

        Response response = apiClient.createOrder(token, order);
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    public void creatingOrderWithoutTokenIsRejected() {
        Response response = apiClient.createOrder("invalid-token", TestDataFactory.validOrder());
        Assert.assertEquals(response.statusCode(), 401);
    }

    @Test
    public void fetchingNonexistentOrderReturns404() {
        String token = login();
        Response response = apiClient.getOrder(token, "ORD-does-not-exist");
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    public void fetchingOrderWithoutTokenIsRejected() {
        Response response = apiClient.getOrder("invalid-token", "ORD-1");
        Assert.assertEquals(response.statusCode(), 401);
    }

    private String login() {
        Response loginResponse = apiClient.login(TestConfig.validEmail(), TestConfig.validPassword());
        return loginResponse.jsonPath().getString("token");
    }
}
