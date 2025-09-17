package com.connecthr.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class ChromeDriverFactory {

    public static WebDriver createDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Check if running in Jenkins or any CI environment
        boolean isCI = System.getenv("CI") != null || System.getenv("JENKINS_HOME") != null;

        if (isCI) {
            // Always run headless in Jenkins
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            System.out.println("🌐 Running in CI environment (headless mode enabled)");
        } else {
            // Local debug mode
            options.addArguments("--start-maximized");
            System.out.println("💻 Running locally (visible browser)");
        }

        return new ChromeDriver(options);
    }
}
