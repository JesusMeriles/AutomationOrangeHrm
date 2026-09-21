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

/**
 * BasePage provides reusable wrapper methods for interacting with web elements,
 * utilizing explicit waits (WebDriverWait) and structured logging.
 */
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Logger log;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        this.log = LogManager.getLogger(this.getClass());
    }

    /**
     * Waits for an element to be visible on the DOM and page.
     */
    protected WebElement waitForVisibility(By locator) {
        log.debug("Waiting for visibility of element: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for an element to be clickable.
     */
    protected WebElement waitForClickable(By locator) {
        log.debug("Waiting for element to be clickable: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Clicks on an element after waiting for it to be clickable.
     */
    protected void click(By locator) {
        log.info("Clicking on element: {}", locator);
        waitForClickable(locator).click();
    }

    /**
     * Sends keys to an input field after clearing existing text.
     */
    protected void sendKeys(By locator, String text) {
        log.info("Entering text into element: {}", locator);
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Retrieves visible text from an element.
     */
    protected String getText(By locator) {
        String text = waitForVisibility(locator).getText();
        log.info("Retrieved text '{}' from element: {}", text, locator);
        return text;
    }

    /**
     * Checks whether an element is displayed.
     */
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

    /**
     * Returns the current page URL.
     */
    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        log.info("Current URL: {}", url);
        return url;
    }

    /**
     * Returns the current page title.
     */
    public String getTitle() {
        String title = driver.getTitle();
        log.info("Page title: {}", title);
        return title;
    }
}
