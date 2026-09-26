package com.orangehrm.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class DriverFactory {
    private static final Logger log = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Initializes a WebDriver instance based on browser name and headless flag.
     *
     * @param browser  name of the browser (chrome, firefox, edge)
     * @param headless whether to run in headless mode
     * @return WebDriver instance
     */
    public static WebDriver initDriver(String browser, boolean headless) {
        log.info("Initializing WebDriver for browser: {} | Headless: {}", browser, headless);
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                    firefoxOptions.addArguments("--window-size=1920,1080");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--window-size=1920,1080");
                }
                edgeOptions.addArguments("--start-maximized");
                driver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-search-engine-choice-screen");
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        if (!headless) {
            driver.manage().window().maximize();
        }

        driverThreadLocal.set(driver);
        log.info("WebDriver initialized successfully for thread: {}", Thread.currentThread().getId());
        return getDriver();
    }

    /**
     * Retrieves the current thread's WebDriver instance.
     *
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                log.info("Closing WebDriver for thread: {}", Thread.currentThread().getId());
                driver.quit();
            } catch (Exception e) {
                log.error("Error closing WebDriver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
