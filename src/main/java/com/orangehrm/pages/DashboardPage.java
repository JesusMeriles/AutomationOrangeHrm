package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
public class DashboardPage extends BasePage {

    private final By headerTitle = By.cssSelector(".oxd-topbar-header-breadcrumb h6");
    private final By userDropdownTab = By.cssSelector(".oxd-userdropdown-tab");
    private final By userDropdownName = By.cssSelector(".oxd-userdropdown-name");
    private final By logoutLink = By.xpath("//a[contains(@href, 'logout')]");
    private final By sideMenuItems = By.cssSelector(".oxd-main-menu-item");
    private final By dashboardWidget = By.cssSelector(".orangehrm-dashboard-widget");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDashboardHeaderDisplayed() {
        return isDisplayed(headerTitle);
    }
    public String getHeaderTitle() {
        return getText(headerTitle);
    }
    public String getUserDropdownName() {
        
        return getText(userDropdownName);
    }

    /**
     * Opens the user dropdown and clicks Logout.
     *
     * @return LoginPage instance
     */
    public LoginPage logout() {
        log.info("Logging out from Dashboard");
        click(userDropdownTab);
        click(logoutLink);
        return new LoginPage(driver);
    }
    public int getSideMenuItemsCount() {
        int count = driver.findElements(sideMenuItems).size();
        log.info("Count of side menu items found: {}", count);
        return count;
    }
    public boolean isWidgetDisplayed() {

        return isDisplayed(dashboardWidget);
    }
}
