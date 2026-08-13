package org.example.tests;

import io.restassured.response.Response;
import org.example.base.BaseTest;
import org.example.config.TestConfig;
import org.example.data.TestDataFactory;
import org.example.model.OrderRequest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PaymentApiTest extends BaseTest {

    @Test
    public void duplicatePaymentOnConfirmedOrderIsRejected() {
        String token = login();
        String orderId = createOrder(token);

        Response firstPayment = apiClient.processPayment(token, orderId, TestConfig.successfulCard());
        Assert.assertEquals(firstPayment.statusCode(), 200);

        Response secondPayment = apiClient.processPayment(token, orderId, TestConfig.successfulCard());
        Assert.assertEquals(secondPayment.statusCode(), 409);
    }

    @Test
    public void paymentForNonexistentOrderReturns404() {
        String token = login();
        Response response = apiClient.processPayment(token, "ORD-does-not-exist", TestConfig.successfulCard());
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    public void paymentWithoutTokenIsRejected() {
        Response response = apiClient.processPayment("invalid-token", "ORD-1", TestConfig.successfulCard());
        Assert.assertEquals(response.statusCode(), 401);
    }

    private String login() {
        Response loginResponse = apiClient.login(TestConfig.validEmail(), TestConfig.validPassword());
        return loginResponse.jsonPath().getString("token");
    }

    private String createOrder(String token) {
        OrderRequest order = TestDataFactory.validOrder();
        Response response = apiClient.createOrder(token, order);
        return response.jsonPath().getString("id");
    }
}
