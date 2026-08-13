package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("loginButton");
    private final By loginError = By.id("loginError");
    private final By orderSection = By.id("orderSection");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl);
        return this;
    }

    public OrderPage loginSuccessfully(String email, String password) {
        driver.findElement(emailInput).sendKeys(email);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(loginButton).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(orderSection));
        return new OrderPage(driver);
    }

    public LoginPage loginExpectingFailure(String email, String password) {
        driver.findElement(emailInput).sendKeys(email);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(loginButton).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(loginError));
        return this;
    }

    public String getErrorMessage() {
        return driver.findElement(loginError).getText();
    }

    public boolean isOrderSectionVisible() {
        return driver.findElement(orderSection).isDisplayed();
    }
}
