package com.orangehrm.tests;

import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test cases for OrangeHRM Login functionality.
 */
public class LoginTest extends BaseTest {

    @Test(description = "Verify successful login with valid credentials redirects to Dashboard")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);

        log.info("Verifying login page is loaded");
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page elements should be visible");

        log.info("Logging in with valid credentials: {}", ConfigReader.getDefaultUsername());
        DashboardPage dashboardPage = loginPage.login(
                ConfigReader.getDefaultUsername(),
                ConfigReader.getDefaultPassword()
        );

        log.info("Verifying Dashboard header");
        Assert.assertTrue(dashboardPage.isDashboardHeaderDisplayed(), "Dashboard header should be visible");
        Assert.assertEquals(dashboardPage.getHeaderTitle(), "Dashboard", "Header text should be 'Dashboard'");
        Assert.assertTrue(dashboardPage.getCurrentUrl().contains("dashboard"), "URL should contain 'dashboard'");
    }

    @Test(description = "Verify invalid credentials display an error alert")
    public void testInvalidLoginCredentials() {
        LoginPage loginPage = new LoginPage(driver);

        log.info("Attempting login with invalid credentials");
        loginPage.login("wrongUser", "wrongPass123");

        log.info("Verifying error message is displayed");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid credentials");
        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid credentials", "Error text should match expected message");
    }

    @Test(description = "Verify user can log out successfully back to login screen")
    public void testSuccessfulLogout() {
        LoginPage loginPage = new LoginPage(driver);

        DashboardPage dashboardPage = loginPage.login(
                ConfigReader.getDefaultUsername(),
                ConfigReader.getDefaultPassword()
        );

        Assert.assertTrue(dashboardPage.isDashboardHeaderDisplayed(), "Must be on Dashboard before logging out");

        LoginPage postLogoutPage = dashboardPage.logout();

        log.info("Verifying redirection back to Login page");
        Assert.assertTrue(postLogoutPage.isLoginPageDisplayed(), "Login page should be visible after logout");
        Assert.assertTrue(postLogoutPage.getCurrentUrl().contains("auth/login"), "URL should contain 'auth/login'");
    }
}
