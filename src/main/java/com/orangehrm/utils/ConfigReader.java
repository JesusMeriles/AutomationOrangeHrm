package com.orangehrm.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
                log.info("config.properties loaded successfully.");
            } else {
                log.error("Unable to find config.properties file in resources folder.");
            }
        } catch (IOException e) {
            log.error("Error reading config.properties file", e);
        }
    }

    /**
     * Gets property value with System properties override support.
     *
     * @param key property key
     * @return property value
     */
    public static String getProperty(String key) {
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.trim().isEmpty()) {
            return systemProp.trim();
        }
        String fileProp = properties.getProperty(key);
        return fileProp != null ? fileProp.trim() : null;
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getBrowser() {
        String browser = getProperty("browser");
        return (browser != null) ? browser.toLowerCase() : "chrome";
    }

    public static boolean isHeadless() {
        String headless = getProperty("headless");
        return "true".equalsIgnoreCase(headless);
    }

    public static int getExplicitWait() {
        String wait = getProperty("explicit.wait");
        try {
            return wait != null ? Integer.parseInt(wait) : 15;
        } catch (NumberFormatException e) {
            return 15;
        }
    }

    public static String getDefaultUsername() {
        return getProperty("default.username");
    }

    public static String getDefaultPassword() {
        return getProperty("default.password");
    }
}
