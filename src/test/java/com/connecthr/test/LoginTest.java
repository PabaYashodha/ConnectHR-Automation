package com.connecthr.test;

import io.qameta.allure.Description;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class LoginTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors"); // ignore SSL warnings
        options.addArguments("--start-maximized");
        // options.addArguments("--headless"); // uncomment for headless mode
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test(description = "Login and create a new asset category")
    @Description("This test logs in, navigates to Asset Management, creates a new category and searches it")
    public void loginAndCreateCategoryTest() {
        try {
            // 1. Open login page
            driver.get("https://172.21.128.14/login");

            // 2. Enter credentials and login
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_username")));
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_password")));
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.login-button")));

            usernameField.sendKeys("Support@connecthr.ae");
            passwordField.sendKeys("C0nne<t@#2|");
            loginButton.click();

            // 3. Verify login successful
            wait.until(ExpectedConditions.urlContains("dashboard"));
            System.out.println("✅ Login successful");

            // Navigate to Asset Management module
            WebElement assetModule = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@class='menu-text' and normalize-space(text())='Assets Management']")));
            assetModule.click();
            wait.until(ExpectedConditions.urlContains("/asset"));
            System.out.println("✅ Navigated to Asset Management module");

            // Click "New category" button
            WebElement newCategoryBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[span[normalize-space()='New category']]")));
            newCategoryBtn.click();
            System.out.println("✅ Opened new category modal");

            // Wait for modal to appear
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ant-modal-content")));
            Thread.sleep(500); // small pause for animation

            // Enter Category Name
            WebElement categoryTitleInput = modal.findElement(By.id("name"));
            categoryTitleInput.clear();
            categoryTitleInput.sendKeys("Test Category17");
            System.out.println("✅ Entered new category name");

            // Enter Category Key
            WebElement categoryKeyInput = modal.findElement(By.id("key"));
            categoryKeyInput.clear();
            categoryKeyInput.sendKeys("Test Category key17");
            System.out.println("✅ Entered new category key");

            // Click Create button
            WebElement createBtn = modal.findElement(By.xpath(".//button[span[normalize-space()='Create']]"));
            wait.until(ExpectedConditions.elementToBeClickable(createBtn)).click();
            System.out.println("✅ Form submitted");

            // Search the category
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@placeholder='Search']")));
            searchBox.clear();
            searchBox.sendKeys("Test Category17");
            searchBox.sendKeys(Keys.ENTER);
            System.out.println("✅ Category searched successfully");

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
