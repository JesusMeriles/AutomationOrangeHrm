package com.orangehrm.pages;

import com.orangehrm.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Logger log;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        this.log = LogManager.getLogger(this.getClass());
    }

    protected WebElement waitForVisibility(By locator) {
        log.debug("Waiting for visibility of element: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        log.debug("Waiting for element to be clickable: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    protected void click(By locator) {
        log.info("Clicking on element: {}", locator);
        waitForClickable(locator).click();
    }
    protected void sendKeys(By locator, String text) {
        log.info("Entering text into element: {}", locator);
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }
    protected String getText(By locator) {
        String text = waitForVisibility(locator).getText();
        log.info("Retrieved text '{}' from element: {}", text, locator);
        return text;
    }
    protected boolean isDisplayed(By locator) {
        try {
            boolean displayed = waitForVisibility(locator).isDisplayed();
            log.debug("Element {} is displayed: {}", locator, displayed);
            return displayed;
        } catch (Exception e) {
            log.debug("Element {} is not displayed: {}", locator, e.getMessage());
            return false;
        }
    }

    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        log.info("Current URL: {}", url);
        return url;
    }
    public String getTitle() {
        String title = driver.getTitle();
        log.info("Page title: {}", title);
        return title;
    }
}
