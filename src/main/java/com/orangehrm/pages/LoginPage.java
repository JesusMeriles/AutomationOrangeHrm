package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object representing OrangeHRM Login Page.
 */
public class LoginPage extends BasePage {

    // Locators
    private final By usernameInput = By.name("username");
    private final By passwordInput = By.name("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By errorMessage = By.cssSelector(".oxd-alert-content-text");
    private final By loginTitle = By.cssSelector(".orangehrm-login-title");
    private final By forgotPasswordLink = By.cssSelector(".orangehrm-login-forgot");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Checks if login page is loaded.
     */
    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginButton) && isDisplayed(usernameInput);
    }

    /**
     * Enters username.
     */
    public LoginPage enterUsername(String username) {
        sendKeys(usernameInput, username);
        return this;
    }

    /**
     * Enters password.
     */
    public LoginPage enterPassword(String password) {
        sendKeys(passwordInput, password);
        return this;
    }

    /**
     * Clicks the login submit button.
     */
    public void clickLoginButton() {
        click(loginButton);
    }

    /**
     * Performs a full login flow with provided credentials.
     *
     * @param username username string
     * @param password password string
     * @return DashboardPage instance
     */
    public DashboardPage login(String username, String password) {
        log.info("Performing login for user: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return new DashboardPage(driver);
    }

    /**
     * Retrieves the error message text shown upon invalid login.
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Checks if error message is displayed.
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    /**
     * Gets the login header/title text (typically "Login").
     */
    public String getLoginTitle() {
        return getText(loginTitle);
    }
}
