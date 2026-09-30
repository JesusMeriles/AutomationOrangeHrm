package com.orangehrm.tests;

import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.PimPage;
import com.orangehrm.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PimTest extends BaseTest {

    /**
     * DataProvider with 2 distinct employees, keeping the easter egg tribute to Dr. Henry "Hank" Pym
     * and Janet Van Dyne from Marvel lore, along with employee photo upload.
     */
    @DataProvider(name = "pimEmployeeDataProvider")
    public Object[][] pimEmployeeDataProvider() {
        String photoPath = PimPage.getDefaultEmployeePhotoPath();
        return new Object[][]{
                // Empleado 1: Dr. Henry Jonathan Pym (Hank Pym / Ant-Man)
                {"Henry", "Jonathan", "Pym", photoPath},
                // Empleado 2: Janet Hope Van Dyne (The Wasp - colega y compañera de Pym)
                {"Janet", "Hope", "Van Dyne", photoPath}
        };
    }

    @Test(dataProvider = "pimEmployeeDataProvider",
            description = "CP-PIM-POS-01: Verify adding a new employee with photo and verifying presence in Employee List")
    public void testAddNewEmployeeWithPhotoAndVerifyInList(String firstName, String middleName, String lastName, String photoPath) {
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

        String uniqueSuffix = String.valueOf((System.currentTimeMillis() + (long) (Math.random() * 1000)) % 1000000);
        String uniqueLastName = lastName + uniqueSuffix;
        String employeeId = "EMP" + uniqueSuffix;

        log.info("Step 4: Creating new employee with ID: {}, Name: {} {} {}, Photo: {}",
                employeeId, firstName, middleName, uniqueLastName, photoPath);
        pimPage.addEmployee(firstName, middleName, uniqueLastName, employeeId, photoPath);

        boolean toastSeen = pimPage.isSuccessToastDisplayed();
        log.info("Toast confirmation observed: {}", toastSeen);

        log.info("Step 5: Navigating to Employee List and searching by Employee ID: {}", employeeId);
        pimPage.searchByEmployeeId(employeeId);

        log.info("Step 6: Verifying employee appears in the Employee List table");
        boolean isIdFound = pimPage.isEmployeePresentInList(employeeId);
        Assert.assertTrue(isIdFound, "The created employee with ID '" + employeeId + "' should be listed in the table");

        boolean isNameFound = pimPage.isEmployeeNamePresentInList(firstName, uniqueLastName);
        Assert.assertTrue(isNameFound, "The created employee name '" + firstName + " " + uniqueLastName + "' should be present in the table");
    }

    @Test(description = "CP-PIM-NEG-01: Verify validation message 'Required' when First Name is empty")
    public void testAddEmployeeMissingFirstName() {
        LoginPage loginPage = new LoginPage(driver);

        log.info("Step 1: Logging into OrangeHRM");
        DashboardPage dashboardPage = loginPage.login(
                ConfigReader.getDefaultUsername(),
                ConfigReader.getDefaultPassword()
        );
        PimPage pimPage = dashboardPage.goToPimModule();

        log.info("Step 2: Navigating to 'Add Employee' tab");
        pimPage.clickAddEmployeeTab();

        log.info("Step 3: Entering Last Name while leaving First Name blank");
        pimPage.setLastName("Pym");

        log.info("Step 4: Submitting form by clicking Save");
        pimPage.clickSaveButton();

        log.info("Step 5: Verifying 'Required' validation error appears under First Name");
        Assert.assertTrue(pimPage.isFirstNameErrorDisplayed(), "Error message should be displayed under First Name field");
        Assert.assertEquals(pimPage.getFirstNameErrorMessage(), "Required", "Error text should be 'Required'");
    }

    @Test(description = "CP-PIM-NEG-02: Verify validation message 'Required' when Last Name is empty")
    public void testAddEmployeeMissingLastName() {
        LoginPage loginPage = new LoginPage(driver);

        log.info("Step 1: Logging into OrangeHRM");
        DashboardPage dashboardPage = loginPage.login(
                ConfigReader.getDefaultUsername(),
                ConfigReader.getDefaultPassword()
        );
        PimPage pimPage = dashboardPage.goToPimModule();

        log.info("Step 2: Navigating to 'Add Employee' tab");
        pimPage.clickAddEmployeeTab();

        log.info("Step 3: Entering First Name while leaving Last Name blank");
        pimPage.setFirstName("Henry");

        log.info("Step 4: Submitting form by clicking Save");
        pimPage.clickSaveButton();

        log.info("Step 5: Verifying 'Required' validation error appears under Last Name");
        Assert.assertTrue(pimPage.isLastNameErrorDisplayed(), "Error message should be displayed under Last Name field");
        Assert.assertEquals(pimPage.getLastNameErrorMessage(), "Required", "Error text should be 'Required'");
    }

    @Test(description = "CP-PIM-NEG-03: Verify error message when Employee ID already exists")
    public void testAddEmployeeDuplicateId() {
        LoginPage loginPage = new LoginPage(driver);

        log.info("Step 1: Logging into OrangeHRM");
        DashboardPage dashboardPage = loginPage.login(
                ConfigReader.getDefaultUsername(),
                ConfigReader.getDefaultPassword()
        );
        PimPage pimPage = dashboardPage.goToPimModule();

        log.info("Step 2: Obtaining an existing Employee ID from table to test duplication");
        pimPage.clickEmployeeListTab();
        String existingId = pimPage.getFirstEmployeeIdFromTable();

        if (existingId == null || existingId.trim().isEmpty()) {
            pimPage.clickAddEmployeeTab();
            existingId = "DUP" + (System.currentTimeMillis() % 100000);
            pimPage.addEmployee("Henry", "J", "Pym", existingId);
        }

        log.info("Step 3: Navigating to Add Employee to attempt creating employee with duplicate ID: {}", existingId);
        pimPage.clickAddEmployeeTab();
        pimPage.setFirstName("Hank");
        pimPage.setLastName("Pym");
        pimPage.setEmployeeId(existingId);

        log.info("Step 4: Submitting form with duplicate ID");
        pimPage.clickSaveButton();

        log.info("Step 5: Verifying 'Employee Id already exists' validation error is displayed");
        Assert.assertTrue(pimPage.isEmployeeIdErrorDisplayed(),
                "Error message should be displayed for duplicate Employee ID");
        Assert.assertEquals(pimPage.getEmployeeIdErrorMessage(), "Employee Id already exists",
                "Error message should state 'Employee Id already exists'");
    }
}
