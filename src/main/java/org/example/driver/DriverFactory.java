package org.example.driver;

import org.example.config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--window-size=1440,900");
        options.addArguments("--disable-notifications");

        if (TestConfig.isHeadless()) {
            options.addArguments("--headless=new");
        }

        return new ChromeDriver(options);
    }
}
