package org.example.tests;

import org.example.base.BaseTest;
import org.example.config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InvalidLoginTest extends BaseTest {

    @Test
    public void invalidLoginShouldBeRejected() {
        loginPage
                .open(server.getBaseUrl())
                .loginExpectingFailure(TestConfig.validEmail(), TestConfig.invalidPassword());

        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid credentials");
        Assert.assertFalse(loginPage.isOrderSectionVisible());
    }
}
