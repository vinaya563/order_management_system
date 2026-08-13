package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PaymentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cardNumber = By.id("cardNumber");
    private final By payButton = By.id("payButton");
    private final By paymentError = By.id("paymentError");
    private final By confirmationSection = By.id("confirmationSection");

    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 5);
    }

    public ConfirmationPage paySuccessfully(String card) {
        driver.findElement(cardNumber).sendKeys(card);
        driver.findElement(payButton).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationSection));
        return new ConfirmationPage(driver);
    }

    public PaymentPage payExpectingFailure(String card) {
        driver.findElement(cardNumber).sendKeys(card);
        driver.findElement(payButton).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(paymentError));
        return this;
    }

    public String getPaymentError() {
        return driver.findElement(paymentError).getText();
    }
}
