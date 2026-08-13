package org.example.tests;

import org.example.base.BaseTest;
import org.example.config.TestConfig;
import org.example.data.TestDataFactory;
import org.example.model.OrderRequest;
import org.example.pages.OrderPage;
import org.example.pages.PaymentPage;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PaymentFailureTest extends BaseTest {

    @Test
    public void failedPaymentShouldNotConfirmOrder() {
        OrderRequest order = TestDataFactory.validOrder();

        OrderPage orderPage = loginPage
                .open(server.getBaseUrl())
                .loginSuccessfully(TestConfig.validEmail(), TestConfig.validPassword());

        PaymentPage paymentPage = orderPage.createOrder(order);
        String orderId = orderPage.getCreatedOrderId();

        paymentPage.payExpectingFailure(TestConfig.failureCard());
        Assert.assertEquals(paymentPage.getPaymentError(), "Payment failed");

        Response loginResponse = apiClient.login(TestConfig.validEmail(), TestConfig.validPassword());
        String token = loginResponse.jsonPath().getString("token");

        Response orderResponse = apiClient.getOrder(token, orderId);
        Assert.assertEquals(orderResponse.jsonPath().getString("orderStatus"), "PAYMENT_FAILED");
        Assert.assertEquals(orderResponse.jsonPath().getString("paymentStatus"), "FAILED");

        databaseValidator.assertOrderMatches(orderId, order, "PAYMENT_FAILED");
    }
}
