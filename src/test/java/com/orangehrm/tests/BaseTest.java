package com.orangehrm.tests;

import com.orangehrm.driver.DriverFactory;
import com.orangehrm.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * BaseTest provides common test lifecycle hooks (@BeforeMethod, @AfterMethod)
 * for setting up the WebDriver and navigating to the target application.
 */
@Listeners(com.orangehrm.listeners.TestListener.class)
public abstract class BaseTest {
    protected final Logger log = LogManager.getLogger(this.getClass());
    protected WebDriver driver;

    @BeforeMethod
    @Parameters({"browser", "headless"})
    public void setUp(@Optional("") String browserParam, @Optional("") String headlessParam) {
        // Allow parameter override from testng.xml, otherwise use config.properties or System properties
        String browser = (browserParam != null && !browserParam.trim().isEmpty())
                ? browserParam
                : ConfigReader.getBrowser();

        boolean headless = (headlessParam != null && !headlessParam.trim().isEmpty())
                ? Boolean.parseBoolean(headlessParam)
                : ConfigReader.isHeadless();

        log.info("Starting test setup with Browser: {}, Headless: {}", browser, headless);
        driver = DriverFactory.initDriver(browser, headless);

        String baseUrl = ConfigReader.getBaseUrl();
        log.info("Navigating to URL: {}", baseUrl);
        driver.get(baseUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("Tearing down WebDriver after test execution...");
        DriverFactory.quitDriver();
    }

    public WebDriver getDriver() {
        return driver;
    }
}
