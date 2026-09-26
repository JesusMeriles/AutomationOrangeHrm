package com.orangehrm.tests;

import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.ConfigReader;
import org.testng.Assert;

import org.testng.annotations.Test;

public class DashboardTest extends BaseTest {

    @Test(description = "Verify key components are displayed on the Dashboard after login")
    public void testDashboardComponents() {
        LoginPage loginPage = new LoginPage(driver);

        log.info("Logging into OrangeHRM to test Dashboard components");
        DashboardPage dashboardPage = loginPage.login(
                ConfigReader.getDefaultUsername(),
                ConfigReader.getDefaultPassword()
        );

        log.info("Verifying Dashboard Header");
        Assert.assertTrue(dashboardPage.isDashboardHeaderDisplayed(), "Dashboard header should be visible");
        Assert.assertEquals(dashboardPage.getHeaderTitle(), "Dashboard", "Breadcrumb title must be 'Dashboard'");

        log.info("Verifying User Profile Dropdown");
        String userName = dashboardPage.getUserDropdownName();
        log.info("Logged-in User Name displayed: {}", userName);
        Assert.assertFalse(userName.trim().isEmpty(), "User profile name should not be empty");

        log.info("Verifying Sidebar navigation menu items");
        int menuItemsCount = dashboardPage.getSideMenuItemsCount();
        log.info("Total menu items detected: {}", menuItemsCount);
        Assert.assertTrue(menuItemsCount > 5, "Sidebar should contain standard menu items (expected > 5, found " + menuItemsCount + ")");

        log.info("Verifying Dashboard widgets presence");
        Assert.assertTrue(dashboardPage.isWidgetDisplayed(), "At least one dashboard widget should be displayed");
    }
}
