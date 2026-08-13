package org.example.base;

import org.example.api.OmsApiClient;
import org.example.db.DatabaseValidator;
import org.example.driver.DriverFactory;
import org.example.pages.LoginPage;
import org.example.server.MockOmsServer;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public abstract class BaseTest {

    protected static MockOmsServer server;

    protected WebDriver driver;
    protected OmsApiClient apiClient;
    protected DatabaseValidator databaseValidator;
    protected LoginPage loginPage;

    @BeforeSuite(alwaysRun = true)
    public void startServer() throws Exception {
        if (server == null) {
            server = new MockOmsServer();
            server.start();
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        server.clear();

        driver = DriverFactory.createDriver();
        apiClient = new OmsApiClient(server.getBaseUrl());
        databaseValidator = new DatabaseValidator(server.getRepository());
        loginPage = new LoginPage(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void stopServer() {
        if (server != null) {
            server.stop();
        }
    }
}
