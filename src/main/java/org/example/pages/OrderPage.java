package org.example.pages;

import org.example.model.OrderRequest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productName = By.id("productName");
    private final By quantity = By.id("quantity");
    private final By unitPrice = By.id("unitPrice");
    private final By createOrderButton = By.id("createOrderButton");
    private final By orderId = By.id("orderId");
    private final By paymentSection = By.id("paymentSection");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 5);
    }

    public PaymentPage createOrder(OrderRequest order) {
        driver.findElement(productName).sendKeys(order.productName());
        driver.findElement(quantity).sendKeys(String.valueOf(order.quantity()));
        driver.findElement(unitPrice).sendKeys(order.unitPrice().toString());
        driver.findElement(createOrderButton).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(orderId));
        wait.until(ExpectedConditions.visibilityOfElementLocated(paymentSection));

        return new PaymentPage(driver);
    }

    public String getCreatedOrderId() {
        return driver.findElement(orderId).getText();
    }
}
