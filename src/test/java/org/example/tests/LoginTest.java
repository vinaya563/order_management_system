package org.example.tests;

import org.example.base.BaseTest;
import org.example.config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void invalidPasswordIsRejected() {
        loginPage
                .open(server.getBaseUrl())
                .loginExpectingFailure(TestConfig.validEmail(), TestConfig.invalidPassword());

        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid credentials");
        Assert.assertFalse(loginPage.isOrderSectionVisible());
    }

    @Test
    public void blankCredentialsAreRejected() {
        loginPage
                .open(server.getBaseUrl())
                .loginExpectingFailure("", "");

        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid credentials");
        Assert.assertFalse(loginPage.isOrderSectionVisible());
    }

    @Test
    public void unknownEmailIsRejected() {
        loginPage
                .open(server.getBaseUrl())
                .loginExpectingFailure("nobody@example.com", TestConfig.validPassword());

        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid credentials");
        Assert.assertFalse(loginPage.isOrderSectionVisible());
    }
}
