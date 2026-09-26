package com.orangehrm.listeners;

import com.orangehrm.driver.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {
    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("==================================================");
        log.info("Starting Test Suite: {}", context.getName());
        log.info("==================================================");
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("==================================================");
        log.info("Finished Test Suite: {}", context.getName());
        log.info("Passed: {}, Failed: {}, Skipped: {}",
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
        log.info("==================================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info(">>> Running Test: {}#{}", result.getTestClass().getName(), result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info(">>> Test PASSED: {}#{}", result.getTestClass().getName(), result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error(">>> Test FAILED: {}#{}", result.getTestClass().getName(), result.getMethod().getMethodName());
        log.error("Failure Cause: ", result.getThrowable());

        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            takeScreenshot(driver, result.getMethod().getMethodName());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn(">>> Test SKIPPED: {}#{}", result.getTestClass().getName(), result.getMethod().getMethodName());
    }

    private void takeScreenshot(WebDriver driver, String testName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String destPath = "screenshots/" + testName + "_" + timestamp + ".png";
            File destination = new File(destPath);
            FileUtils.copyFile(source, destination);
            log.info("Screenshot saved successfully to: {}", destination.getAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
        }
    }
}
