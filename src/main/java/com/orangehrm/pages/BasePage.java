package com.orangehrm.pages;

import com.orangehrm.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

    protected WebElement waitForPresence(By locator) {
        log.debug("Waiting for presence of element: {}", locator);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        log.debug("Waiting for element to be clickable: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        log.info("Clicking on element: {}", locator);
        waitForClickable(locator).click();
    }

    /**
     * Uploads a file to an <input type="file"> element using its presence in DOM.
     * Does not require element visibility since file inputs are often styled/hidden.
     */
    protected void uploadFile(By locator, String absoluteFilePath) {
        log.info("Uploading file '{}' to locator: {}", absoluteFilePath, locator);
        WebElement fileInput = waitForPresence(locator);
        fileInput.sendKeys(absoluteFilePath);
    }
    protected void sendKeys(By locator, String text) {
        log.info("Entering text into element: {}", locator);
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Clears an input using keyboard shortcuts (CTRL+A, Backspace) and types the new text.
     * Useful for reactive framework inputs (like Vue.js in OrangeHRM) where element.clear()
     * might not trigger the value-change event.
     */
    protected void clearAndSendKeys(By locator, String text) {
        log.info("Clearing with keyboard and entering text into element: {}", locator);
        WebElement element = waitForVisibility(locator);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        element.sendKeys(text);
    }

    /**
     * Retrieves the 'value' attribute of an input field.
     */
    protected String getValue(By locator) {
        String value = waitForVisibility(locator).getAttribute("value");
        log.info("Retrieved attribute value '{}' from element: {}", value, locator);
        return value;
    }

    /**
     * Waits until the specified element is invisible or detached from the DOM.
     */
    protected boolean waitForInvisibility(By locator) {
        log.debug("Waiting for invisibility of element: {}", locator);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits for the OrangeHRM loading spinner to disappear.
     */
    public void waitForSpinnerToDisappear() {
        By spinner = By.cssSelector(".oxd-loading-spinner");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
        } catch (Exception ignored) {
            // Spinner might not appear if response is immediate
        }
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
