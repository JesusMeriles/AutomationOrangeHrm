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

    // Profile Photo Locators
    private final By employeePhotoInput = By.cssSelector("input[type='file'].oxd-file-input, input[type='file']");
    private final By employeePhotoPreview = By.cssSelector("img.employee-image");

    // Add Employee Validation Error Locators
    private final By firstNameError = By.xpath("//input[@name='firstName']/ancestor::div[contains(@class,'oxd-input-group')][1]//span[contains(@class,'oxd-input-field-error-message')] | //input[@name='firstName']/following-sibling::span[contains(@class,'oxd-input-field-error-message')]");
    private final By lastNameError = By.xpath("//input[@name='lastName']/ancestor::div[contains(@class,'oxd-input-group')][1]//span[contains(@class,'oxd-input-field-error-message')] | //input[@name='lastName']/following-sibling::span[contains(@class,'oxd-input-field-error-message')]");
    private final By employeeIdError = By.xpath("//label[text()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//span[contains(@class,'oxd-input-field-error-message')] | //span[contains(@class,'oxd-input-field-error-message') and contains(text(),'already exists')]");

    // Operation Tracking
    private boolean lastOperationSuccess = false;

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
     * Resolves the absolute path for the employee photo located in resources/images.
     */
    public static String getDefaultEmployeePhotoPath() {
        java.io.File file = new java.io.File("src/main/resources/images/employee-photo.jpg");
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        var resource = PimPage.class.getClassLoader().getResource("images/employee-photo.jpg");
        if (resource != null) {
            try {
                return new java.io.File(resource.toURI()).getAbsolutePath();
            } catch (Exception ignored) {
                return new java.io.File(resource.getPath()).getAbsolutePath();
            }
        }
        return file.getAbsolutePath();
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
     * Uploads the employee profile photo using the file input.
     *
     * @param photoPath absolute or relative path to the image file
     */
    public PimPage uploadProfilePhoto(String photoPath) {
        if (photoPath != null && !photoPath.trim().isEmpty()) {
            log.info("Uploading employee profile photo: {}", photoPath);
            java.io.File file = new java.io.File(photoPath);
            uploadFile(employeePhotoInput, file.getAbsolutePath());
        }
        return this;
    }

    /**
     * Checks if the employee photo preview element is present and displayed.
     */
    public boolean isProfilePhotoDisplayed() {
        return isDisplayed(employeePhotoPreview);
    }

    /**
     * Fills the Add Employee form, optionally uploads a photo, and submits it.
     *
     * @param firstName  first name of employee
     * @param middleName middle name (optional, can be empty)
     * @param lastName   last name of employee
     * @param employeeId unique employee id
     * @param photoPath  path to image file (optional, can be null or empty)
     */
    public PimPage addEmployee(String firstName, String middleName, String lastName, String employeeId, String photoPath) {
        log.info("Filling Add Employee form: {} {} {}, ID: {}, Photo: {}", firstName, middleName, lastName, employeeId, photoPath);

        if (photoPath != null && !photoPath.trim().isEmpty()) {
            uploadProfilePhoto(photoPath);
        }

        if (firstName != null && !firstName.isEmpty()) {
            sendKeys(firstNameInput, firstName);
        }

        if (middleName != null && !middleName.trim().isEmpty()) {
            sendKeys(middleNameInput, middleName);
        }

        if (lastName != null && !lastName.isEmpty()) {
            sendKeys(lastNameInput, lastName);
        }

        if (employeeId != null && !employeeId.trim().isEmpty()) {
            clearAndSendKeys(employeeIdInput, employeeId);
        }

        click(saveButton);
        waitForSaveToComplete();
        return this;
    }

    /**
     * Fills the Add Employee form and submits it without a photo.
     *
     * @param firstName  first name of employee
     * @param middleName middle name (optional, can be empty)
     * @param lastName   last name of employee
     * @param employeeId unique employee id
     */
    public PimPage addEmployee(String firstName, String middleName, String lastName, String employeeId) {
        return addEmployee(firstName, middleName, lastName, employeeId, null);
    }

    /**
     * Enters First Name into the form without submitting.
     */
    public PimPage setFirstName(String firstName) {
        sendKeys(firstNameInput, firstName);
        return this;
    }

    /**
     * Enters Last Name into the form without submitting.
     */
    public PimPage setLastName(String lastName) {
        sendKeys(lastNameInput, lastName);
        return this;
    }

    /**
     * Sets or modifies Employee ID into the form without submitting.
     */
    public PimPage setEmployeeId(String employeeId) {
        clearAndSendKeys(employeeIdInput, employeeId);
        return this;
    }

    /**
     * Clicks the save button directly without waiting for redirect (useful for negative tests).
     */
    public PimPage clickSaveButton() {
        log.info("Clicking Save button directly");
        click(saveButton);
        return this;
    }

    /**
     * Gets the error message displayed under First Name field.
     */
    public String getFirstNameErrorMessage() {
        return getText(firstNameError);
    }

    /**
     * Checks if First Name error message is displayed.
     */
    public boolean isFirstNameErrorDisplayed() {
        return isDisplayed(firstNameError);
    }

    /**
     * Gets the error message displayed under Last Name field.
     */
    public String getLastNameErrorMessage() {
        return getText(lastNameError);
    }

    /**
     * Checks if Last Name error message is displayed.
     */
    public boolean isLastNameErrorDisplayed() {
        return isDisplayed(lastNameError);
    }

    /**
     * Gets the error message displayed under Employee ID field.
     */
    public String getEmployeeIdErrorMessage() {
        return getText(employeeIdError);
    }

    /**
     * Checks if Employee ID error message is displayed.
     */
    public boolean isEmployeeIdErrorDisplayed() {
        return isDisplayed(employeeIdError);
    }

    /**
     * Waits for the save request to complete (toast notification or page redirect).
     */
    public void waitForSaveToComplete() {
        try {
            waitForVisibility(successToast);
            lastOperationSuccess = true;
            log.info("Success toast observed upon employee creation");
        } catch (Exception e) {
            log.debug("Toast not caught directly: {}", e.getMessage());
            lastOperationSuccess = false;
        }
        waitForSpinnerToDisappear();
    }

    /**
     * Checks if the success notification toast is displayed.
     */
    public boolean isSuccessToastDisplayed() {
        if (lastOperationSuccess) {
            return true;
        }
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

    /**
     * Retrieves the Employee ID of the first record displayed in the Employee List table.
     */
    public String getFirstEmployeeIdFromTable() {
        waitForSpinnerToDisappear();
        By firstIdLocator = By.xpath("//div[contains(@class,'oxd-table-card')][1]//div[@role='cell'][2]");
        try {
            String id = getText(firstIdLocator).trim();
            log.info("Retrieved first Employee ID from table: '{}'", id);
            return id;
        } catch (Exception e) {
            log.warn("Could not retrieve first Employee ID from table: {}", e.getMessage());
            return "";
        }
    }
}
