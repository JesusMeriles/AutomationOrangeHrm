package com.orangehrm.tests;

import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.PimPage;
import com.orangehrm.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PimTest extends BaseTest {

    @Test(description = "CP-PIM-POS-01: Verify adding a new employee and verifying its presence in the Employee List")
    public void testAddNewEmployeeAndVerifyInList() {
        LoginPage loginPage = new LoginPage(driver);

        log.info("Step 1: Logging into OrangeHRM");
        DashboardPage dashboardPage = loginPage.login(
                ConfigReader.getDefaultUsername(),
                ConfigReader.getDefaultPassword()
        );
        Assert.assertTrue(dashboardPage.isDashboardHeaderDisplayed(), "Dashboard should be visible after login");

        log.info("Step 2: Navigating to PIM module");
        PimPage pimPage = dashboardPage.goToPimModule();

        log.info("Step 3: Clicking 'Add Employee' tab");
        pimPage.clickAddEmployeeTab();

        // En caso negativo verficar si un ID unico existe <<
        String uniqueSuffix = String.valueOf(System.currentTimeMillis() % 1000000);
        String firstName = "TestUser";
        String middleName = "QA";
        String lastName = "Auto" + uniqueSuffix;
        String employeeId = "ID" + uniqueSuffix;

        log.info("Step 4: Creating new employee with ID: {}, Name: {} {} {}", employeeId, firstName, middleName, lastName);
        pimPage.addEmployee(firstName, middleName, lastName, employeeId);

        // Optional confirmation verification
        boolean toastSeen = pimPage.isSuccessToastDisplayed();
        log.info("Toast confirmation observed: {}", toastSeen);

        log.info("Step 5: Navigating to Employee List and searching by Employee ID: {}", employeeId);
        pimPage.searchByEmployeeId(employeeId);

        log.info("Step 6: Verifying employee appears in the Employee List table");
        boolean isIdFound = pimPage.isEmployeePresentInList(employeeId);
        Assert.assertTrue(isIdFound, "The created employee with ID '" + employeeId + "' should be listed in the table");

        boolean isNameFound = pimPage.isEmployeeNamePresentInList(firstName, lastName);
        Assert.assertTrue(isNameFound, "The created employee name '" + firstName + " " + lastName + "' should be present in the table");
    }

    @Test(description = "CP-PIM-POS-02: Verify adding employee with full name (First, Middle, Last) and verifying in list")
    public void testAddEmployeeFullNameAndVerify() {
        LoginPage loginPage = new LoginPage(driver);

        DashboardPage dashboardPage = loginPage.login(
                ConfigReader.getDefaultUsername(),
                ConfigReader.getDefaultPassword()
        );

        PimPage pimPage = dashboardPage.goToPimModule();
        pimPage.clickAddEmployeeTab();

        String uniqueSuffix = String.valueOf((System.currentTimeMillis() + 7) % 1000000);
        String firstName = "Henry";
        String middleName = "Jonathan";
        String lastName = "Pym" + uniqueSuffix;
        String employeeId = "EMP" + uniqueSuffix;

        pimPage.addEmployee(firstName, middleName, lastName, employeeId);
        pimPage.searchByEmployeeId(employeeId);

        Assert.assertTrue(pimPage.isEmployeePresentInList(employeeId),
                "Employee with ID '" + employeeId + "' should be present in the search results");
    }
}
