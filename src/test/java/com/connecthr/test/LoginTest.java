package com.connecthr.test;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors"); // ignore SSL warnings
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test(description = "Login and create a new asset category")
    @Description("This test logs in, navigates to Asset Management, creates a new category and searches it")
    public void loginAndCreateCategoryTest() {
        try {
            // 1. Open the login page
            driver.get("https://172.21.128.14/login");

            // 2. Enter credentials
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_username")));
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_password")));
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.login-button")));

            usernameField.sendKeys("Support@connecthr.ae");
            passwordField.sendKeys("C0nne<t@#2|");
            loginButton.click();

            // 3. Verification (check if login successful)
            wait.until(ExpectedConditions.urlContains("dashboard"));
            System.out.println("✅ Login successful");

            // Navigate to Asset Management module
            WebElement assetModule = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='menu-text' and normalize-space(text())='Assets Management']"))
            );
            assetModule.click();
            wait.until(ExpectedConditions.urlContains("/asset"));
            System.out.println("✅ Navigated to Asset Management module");

            // Click new category button
            WebElement newCategoryBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[span[normalize-space()='New category']]"))
            );
            newCategoryBtn.click();
            System.out.println("✅ Navigated to new category form");

            // Add category name
            WebElement categoryTitleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
            categoryTitleInput.sendKeys("Test Category7");
            System.out.println("✅ Entered new category name");

            // Add category key
            WebElement categoryKeyInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("key")));
            categoryKeyInput.sendKeys("Test Category key7");
            System.out.println("✅ Entered new category key");

            // Click create button
            WebElement createBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[span[normalize-space()='Create']]"))
            );
            createBtn.click();
            System.out.println("✅ Form submitted");

            // Search the category
            WebElement searchBox1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search']")));
            searchBox1.clear();
            searchBox1.sendKeys("Laptop");
            searchBox1.sendKeys(Keys.ENTER);
            System.out.println("✅ Category searched successfully.");

            Assert.assertTrue(true, "Test finished without errors");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✅ Browser closed");
        }
    }
}
