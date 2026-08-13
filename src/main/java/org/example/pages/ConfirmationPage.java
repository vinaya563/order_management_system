package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ConfirmationPage {

    private final WebDriver driver;

    private final By confirmationMessage = By.id("confirmationMessage");
    private final By orderStatus = By.id("orderStatus");
    private final By displayedOrderId = By.id("confirmationOrderId");

    public ConfirmationPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getConfirmationMessage() {
        return driver.findElement(confirmationMessage).getText();
    }

    public String getDisplayedOrderId() {
        return driver.findElement(displayedOrderId).getText();
    }

    public String getOrderStatus() {
        return driver.findElement(orderStatus).getText();
    }
}
