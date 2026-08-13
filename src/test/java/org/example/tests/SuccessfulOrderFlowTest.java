package org.example.tests;

import org.example.base.BaseTest;
import org.example.config.TestConfig;
import org.example.data.TestDataFactory;
import org.example.model.OrderRequest;
import org.example.pages.ConfirmationPage;
import org.example.pages.OrderPage;
import org.example.pages.PaymentPage;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SuccessfulOrderFlowTest extends BaseTest {

    @Test
    public void userCanCompleteSuccessfulOrderFlow() {
        OrderRequest expectedOrder = TestDataFactory.validOrder();

        Response loginResponse = apiClient.login(
                TestConfig.validEmail(),
                TestConfig.validPassword()
        );
        Assert.assertEquals(loginResponse.statusCode(), 200);
        String token = loginResponse.jsonPath().getString("token");

        OrderPage orderPage = loginPage
                .open(server.getBaseUrl())
                .loginSuccessfully(TestConfig.validEmail(), TestConfig.validPassword());

        PaymentPage paymentPage = orderPage.createOrder(expectedOrder);
        String orderId = orderPage.getCreatedOrderId();

        Assert.assertNotNull(orderId);
        Assert.assertFalse(orderId.isBlank());

        ConfirmationPage confirmationPage = paymentPage.paySuccessfully(TestConfig.successfulCard());

        Assert.assertEquals(confirmationPage.getOrderStatus(), "CONFIRMED");
        Assert.assertEquals(confirmationPage.getDisplayedOrderId(), orderId);

        Response finalOrderResponse = apiClient.getOrder(token, orderId);
        Assert.assertEquals(finalOrderResponse.statusCode(), 200);
        Assert.assertEquals(finalOrderResponse.jsonPath().getString("id"), orderId);
        Assert.assertEquals(finalOrderResponse.jsonPath().getString("orderStatus"), "CONFIRMED");

        databaseValidator.assertOrderMatches(orderId, expectedOrder, "CONFIRMED");
    }
}
