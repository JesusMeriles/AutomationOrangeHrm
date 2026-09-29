package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object representing OrangeHRM PIM (Personnel Information Management) Module,
 * covering Add Employee and Employee List functionality.
 */
public class PimPage extends BasePage {

    // Top Navigation Tabs
    private final By addEmployeeTab = By.xpath("//nav[@aria-label='Topbar Menu']//a[normalize-space()='Add Employee']");
    private final By employeeListTab = By.xpath("//nav[@aria-label='Topbar Menu']//a[normalize-space()='Employee List']");
   //No usar hasta agregar empleado para login 
    private final By addEmployeeButton = By.xpath("//button[normalize-space()='Add' or contains(.,'Add')]");

    // Add Employee Form Locators
    private final By firstNameInput = By.name("firstName");
    private final By middleNameInput = By.name("middleName");
    private final By lastNameInput = By.name("lastName");
    private final By employeeIdInput = By.xpath("//label[text()='Employee Id']/parent::div/following-sibling::div/input");
    private final By saveButton = By.cssSelector("button[type='submit']");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast--success')]");

    // Employee List Search Locators
    private final By searchEmployeeIdInput = By.xpath("//label[text()='Employee Id']/parent::div/following-sibling::div/input");
    private final By searchSubmitButton = By.cssSelector("button[type='submit']");
    private final By searchResetButton = By.cssSelector("button[type='reset']");
    private final By tableCards = By.cssSelector(".oxd-table-body .oxd-table-card");
    private final By recordsFoundText = By.xpath("//span[contains(@class,'oxd-text') and contains(text(),'Record')]");

    public PimPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to the Add Employee tab.
     */
    public PimPage clickAddEmployeeTab() {
        log.info("Navigating to 'Add Employee' tab");
        click(addEmployeeTab);
        waitForSpinnerToDisappear();
        waitForVisibility(firstNameInput);
        return this;
    }

    /**
     * Navigates to the Employee List tab.
     */
    public PimPage clickEmployeeListTab() {
        log.info("Navigating to 'Employee List' tab");
        click(employeeListTab);
        waitForSpinnerToDisappear();
        waitForVisibility(searchEmployeeIdInput);
        return this;
    }

    /**
     * Retrieves the automatically generated Employee ID currently in the input.
     */
    public String getGeneratedEmployeeId() {
        waitForVisibility(employeeIdInput);
        String id = getValue(employeeIdInput);
        log.info("Captured auto-generated Employee ID: {}", id);
        return id;
    }

    /**
     * Fills the Add Employee form and submits it.
     *
     * @param firstName  first name of employee
     * @param middleName middle name (optional, can be empty)
     * @param lastName   last name of employee
     * @param employeeId unique employee id
     */
    public PimPage addEmployee(String firstName, String middleName, String lastName, String employeeId) {
        log.info("Filling Add Employee form: {} {} {}, ID: {}", firstName, middleName, lastName, employeeId);
        sendKeys(firstNameInput, firstName);

        if (middleName != null && !middleName.trim().isEmpty()) {
            sendKeys(middleNameInput, middleName);
        }

        sendKeys(lastNameInput, lastName);

        if (employeeId != null && !employeeId.trim().isEmpty()) {
            clearAndSendKeys(employeeIdInput, employeeId);
        }

        click(saveButton);
        waitForSaveToComplete();
        return this;
    }

    /**
     * Waits for the save request to complete (toast notification or page redirect).
     */
    public void waitForSaveToComplete() {
        try {
            waitForVisibility(successToast);
            log.info("Success toast observed upon employee creation");
        } catch (Exception e) {
            log.debug("Toast not caught directly: {}", e.getMessage());
        }
        waitForSpinnerToDisappear();
    }

    /**
     * Checks if the success notification toast is displayed.
     */
    public boolean isSuccessToastDisplayed() {
        try {
            boolean displayed = waitForVisibility(successToast).isDisplayed();
            log.info("Success toast displayed: {}", displayed);
            return displayed;
        } catch (Exception e) {
            log.warn("Success toast not observed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Searches for an employee in the Employee List by Employee ID.
     *
     * @param employeeId ID to search for
     */
    public PimPage searchByEmployeeId(String employeeId) {
        log.info("Searching employee list for Employee ID: {}", employeeId);
        clickEmployeeListTab();
        waitForSpinnerToDisappear();
        clearAndSendKeys(searchEmployeeIdInput, employeeId);
        click(searchSubmitButton);
        waitForSpinnerToDisappear();
        return this;
    }

    /**
     * Verifies if an employee with the given ID exists in the result table.
     *
     * @param expectedEmployeeId Employee ID to check
     * @return true if found, false otherwise
     */
    public boolean isEmployeePresentInList(String expectedEmployeeId) {
        waitForSpinnerToDisappear();
        By matchingRow = By.xpath("//div[contains(@class,'oxd-table-card')]//div[contains(text(),'" + expectedEmployeeId + "')]");
        try {
            waitForVisibility(matchingRow);
            log.info("Found employee record matching ID '{}'", expectedEmployeeId);
            return true;
        } catch (Exception e) {
            log.warn("Direct locator for ID '{}' not found, scanning rows: {}", expectedEmployeeId, e.getMessage());
            List<WebElement> rows = driver.findElements(tableCards);
            for (WebElement row : rows) {
                if (row.getText().contains(expectedEmployeeId)) {
                    log.info("Found employee record in table cards matching ID '{}'", expectedEmployeeId);
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Verifies if an employee with the given first and last name exists in the table.
     */
    public boolean isEmployeeNamePresentInList(String firstName, String lastName) {
        waitForSpinnerToDisappear();
        By matchingName = By.xpath("//div[contains(@class,'oxd-table-card')]//div[contains(text(),'" + firstName + "')]");
        try {
            waitForVisibility(matchingName);
            log.info("Found employee record matching First Name '{}'", firstName);
            return true;
        } catch (Exception e) {
            List<WebElement> rows = driver.findElements(tableCards);
            for (WebElement row : rows) {
                String rowText = row.getText();
                if (rowText.contains(firstName) && rowText.contains(lastName)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Retrieves the records count text (e.g. '(1) Record Found').
     */
    public String getRecordsFoundText() {
        waitForSpinnerToDisappear();
        try {
            return getText(recordsFoundText);
        } catch (Exception e) {
            return "";
        }
    }
}
